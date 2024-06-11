package com.netflix.conductor.redis.dao.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisTestUtil;
import com.netflix.conductor.redis.config.AnyRedisCondition;

@ActiveProfiles("test")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Configuration
    static class ContextConfiguration {

        @Bean
        @Primary
        public JedisProxy jedisProxy() {
            return RedisTestUtil.createTestJedisProxy();
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public ConductorProperties conductorProperties() {
            return new ConductorProperties();
        }

        @Bean
        public RedisProperties redisProperties() {
            return new RedisProperties();
        }
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Add test cases for each method below

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
        assertEquals("testEventHandler", retrievedEventHandler.getName());
        assertEquals("testEvent", retrievedEventHandler.getEvent());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testEvent2");

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    // Add test cases for updateEventHandler method

    // Add test cases for removeEventHandler method

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent2");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    // Add test cases for index and removeIndex methods

    // Add test cases for getEventHandlersForEvent method

    // Add test cases for getEventHandler method with existing name

    // Add test cases for getEventHandler method with non-existent name
}