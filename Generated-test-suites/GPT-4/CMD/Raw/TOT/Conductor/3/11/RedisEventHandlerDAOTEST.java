import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @Mock
    private ConductorProperties conductorProperties;
    
    @Mock
    private RedisProperties redisProperties;
   
    private RedisEventHandlerDAO redisEventHandlerDAO;


    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler_whenEventHandlerDoesNotExist() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
       
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_whenEventHandlerExist() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        when(jedisProxy.hget(any(), eq(eventHandler.getName()))).thenReturn("{}");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler_whenEventHandlerExist() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        when(jedisProxy.hget(any(), any())).thenReturn("{}");
        when(objectMapper.readValue(any(), any())).thenReturn(eventHandler);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
       
        verify(jedisProxy, times(1)).hdel(any(), any());
        verify(jedisProxy, times(1)).hset(any(), any(), anyString());
    }

    // continue with all methods and edge cases....

}