import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {
    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private RedisTemplate<String, String> redisTemplate;

    @Before
    public void setup() {
        redisTemplate = mock(RedisTemplate.class);
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO.setRedisTemplate(redisTemplate);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        when(redisTemplate.hset(anyString(), anyString(), anyString())).thenReturn(true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertThat(redisEventHandlerDAO.getAllEventHandlers()).contains(eventHandler);
    }

    // Add test cases for updateEventHandler, removeEventHandler, getAllEventHandlers, and other methods here

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        EventHandler existingEventHandler = new EventHandler();
        when(redisTemplate.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = "nonExistentHandler";
        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetEventHandler() {
        String name = "existingHandler";
        EventHandler eventHandler = new EventHandler();
        when(redisTemplate.hget(anyString(), name)).thenReturn(toJson(eventHandler));
        EventHandler result = redisEventHandlerDAO.getEventHandler(name);
        assertThat(result).isEqualTo(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        String name = "nonExistentHandler";
        when(redisTemplate.hget(anyString(), name)).thenReturn(null);
        redisEventHandlerDAO.getEventHandler(name);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        String name = "existingHandler";
        when(redisTemplate.hget(anyString(), name)).thenThrow(new RuntimeException());
        redisEventHandlerDAO.getEventHandler(name);
    }
}