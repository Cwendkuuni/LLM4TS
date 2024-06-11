
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties properties;
    @Mock
    private EventHandler eventHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        when(eventHandler.getName()).thenReturn("test");
        when(redisEventHandlerDAO.getEventHandler("test")).thenReturn(new EventHandler());
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testAddEventHandler_Success() {
        when(eventHandler.getName()).thenReturn("test");
        when(redisEventHandlerDAO.getEventHandler("test")).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

}
