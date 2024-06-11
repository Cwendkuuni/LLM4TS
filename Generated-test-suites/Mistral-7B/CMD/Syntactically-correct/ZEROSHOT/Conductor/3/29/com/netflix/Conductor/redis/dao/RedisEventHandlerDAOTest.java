import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.util.RedisKeys;
import com.netflix.conductor.redis.util.TestUtil;

public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String EVENT = "testEvent";

    private ObjectMapper objectMapper;
    private JedisProxy jedisProxy;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        this.objectMapper = mock(ObjectMapper.class);
        this.jedisProxy = mock(JedisProxy.class);
        this.conductorProperties = mock(ConductorProperties.class);
        this.redisProperties = mock(RedisProperties.class);

        when(this.objectMapper.readValue(Mockito.anyString(), Mockito.eq(EventHandler.class)))
                .then(new Function<String, EventHandler>() {
                    @Override
                    public EventHandler apply(String json) {
                        return new EventHandler() {
                            @Override
                            public String getName() {
                                return EVENT_HANDLER_NAME;
                            }

                            @Override
                            public String getEvent() {
                                return EVENT;
                            }

                            @Override
                            public boolean isActive() {
                                return true;
                            }
                        };
                    }
                });
        when(this.objectMapper.writeValueAsString(Mockito.any(EventHandler.class)))
                .then(new Function<EventHandler, String>() {
                    @Override
                    public String apply(EventHandler input) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", input.getName());
                        jsonObject.put("event", input.getEvent());
                        jsonObject.put("active", input.isActive());
                        return jsonObject.toString();
                    }
                });

        when(this.jedisProxy.hget(Mockito.anyString(), Mockito.eq(EVENT_HANDLERS)))
                .thenReturn(TestUtil.createHash(EVENT_HANDLERS, ImmutableSet.of(EVENT_HANDLER_NAME, "otherHandler")));

        when(this.jedisProxy.hget(Mockito.anyString(), Mockito.eq(EVENT_HANDLERS_BY_EVENT), Mockito.anyString()))
                .thenReturn(TestUtil.createSet(EVENT_HANDLERS_BY_EVENT, EVENT_HANDLER_NAME, "otherHandler"));

        when(this.jedisProxy.hgetAll(Mockito.anyString()))
                .thenReturn(TestUtil.createHash(EVENT_HANDLERS, ImmutableSet.of(EVENT_HANDLER_NAME, "otherHandler")));

        this.redisEventHandlerDAO = new RedisEventHandlerDAO(this.jedisProxy, this.objectMapper, this.conductorProperties, this.redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public String getName() {
                return EVENT_HANDLER_NAME;
            }

            @Override
            public String getEvent() {
                return EVENT;
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };

        redisEventHandlerDAO.addEventHandler(eventHandler);

        ArgumentCaptor<EventHandler> argumentCaptor = ArgumentCaptor.forClass(EventHandler.class);
        verify(this.jedisProxy).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), argumentCaptor.capture(), Mockito.anyString());
        EventHandler capturedEventHandler = argumentCaptor.getValue();
        assertEquals(EVENT_HANDLER_NAME, capturedEventHandler.getName());
        assertEquals(EVENT, capturedEventHandler.getEvent());
        assertEquals(true, capturedEventHandler.isActive());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflictingName() {
        EventHandler existingEventHandler = new EventHandler() {
            @Override
            public String getName() {
                return EVENT_HANDLER_NAME;
            }

            @Override
            public String getEvent() {
                return EVENT;
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler() {
            @Override
            public String getName() {
                return EVENT_HANDLER_NAME;
            }

            @Override
            public String getEvent() {
                return EVENT;
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };
        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public String getName() {
                return EVENT_HANDLER_NAME;
            }

            @Override
            public String getEvent() {
                return EVENT;
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler() {
            @Override
            public String getName() {
                return EVENT_HANDLER_NAME;
            }

            @Override
            public String getEvent() {
                return EVENT_CHANGE;
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        ArgumentCaptor<EventHandler> argumentCaptor = ArgumentCaptor.forClass(EventHandler.class);
        verify(this.jedisProxy).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), argumentCaptor.capture(), Mockito.anyString());
        EventHandler capturedEventHandler = argumentCaptor.getValue();
        assertEquals(EVENT_HANDLER_NAME, capturedEventHandler.getName());
        assertEquals(EVENT_CHANGE, capturedEventHandler.getEvent());
        assertEquals(true, capturedEventHandler.isActive());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentEventHandler() {
        EventHandler updatedEventHandler = new EventHandler() {
            @Override
            public String getName() {
                return "nonExistentEventHandler";
            }

            @Override
            public String getEvent() {
                return EVENT;
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public String getName() {
                return EVENT_HANDLER_NAME;
            }

            @Override
            public String getEvent() {
                return EVENT;
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };

        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.jedisProxy).hdel(Mockito.eq(nsKey(EVENT_HANDLERS)), argumentCaptor.capture());
        assertEquals(EVENT_HANDLER_NAME, argumentCaptor.getValue());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentEventHandler() {
        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> all = TestUtil.createHash(EVENT_HANDLERS, ImmutableSet.of(EVENT_HANDLER_NAME, "otherHandler"));
        when(this.jedisProxy.hgetAll(Mockito.anyString())).thenReturn(all);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, eventHandlers.size());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerWithNonExistentEventHandler() {
        when(this.jedisProxy.hget(Mockito.anyString(), Mockito.eq(EVENT_HANDLERS), Mockito.eq(EVENT_HANDLER_NAME)))
                .thenThrow(new RuntimeException("Mock exception"));

        redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler() {
            @Override
            public String getName() {
                return EVENT_HANDLER_NAME;
            }

            @Override
            public String getEvent() {
                return EVENT;
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };

        when(this.jedisProxy.hget(Mockito.anyString(), Mockito.eq(EVENT_HANDLERS), Mockito.eq(EVENT_HANDLER_NAME)))
                .thenReturn(TestUtil.createHash(EVENT_HANDLERS, ImmutableSet.of(EVENT_HANDLER_NAME)).get(EVENT_HANDLER_NAME));

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
        assertEquals(EVENT_HANDLER_NAME, retrievedEventHandler.getName());
        assertEquals(EVENT, retrievedEventHandler.getEvent());
        assertEquals(true, retrievedEventHandler.isActive());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Map<String, String> allHandlers = TestUtil.createHash(EVENT_HANDLERS, ImmutableSet.of(EVENT_HANDLER_NAME, "otherHandler"));
        Set<String> handlersForEvent = TestUtil.createSet(EVENT_HANDLERS_BY_EVENT, EVENT_HANDLER_NAME, "otherHandler");

        when(this.jedisProxy.hgetAll(Mockito.anyString())).thenReturn(allHandlers);
        when(this.jedisProxy.smembers(Mockito.anyString(), Mockito.eq(EVENT_HANDLERS_BY_EVENT), Mockito.anyString()))
                .thenReturn(handlersForEvent.toArray(new String[handlersForEvent.size()]));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent_ActiveOnly() {
        Map<String, String> allHandlers = TestUtil.createHash(EVENT_HANDLERS, ImmutableSet.of(EVENT_HANDLER_NAME, "otherHandler"));
        Set<String> handlersForEvent = TestUtil.createSet(EVENT_HANDLERS_BY_EVENT, EVENT_HANDLER_NAME);

        when(this.jedisProxy.hgetAll(Mockito.anyString())).thenReturn(allHandlers);
        when(this.jedisProxy.smembers(Mockito.anyString(), Mockito.eq(EVENT_HANDLERS_BY_EVENT), Mockito.anyString()))
                .thenReturn(handlersForEvent.toArray(new String[handlersForEvent.size()]));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
        assertEquals(1, eventHandlers.size());
        assertEquals(EVENT_HANDLER_NAME, eventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEvent_Empty() {
        Map<String, String> allHandlers = Collections.emptyMap();

        when(this.jedisProxy.hgetAll(Mockito.anyString())).thenReturn(allHandlers);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
        assertEquals(0, eventHandlers.size());
    }
}