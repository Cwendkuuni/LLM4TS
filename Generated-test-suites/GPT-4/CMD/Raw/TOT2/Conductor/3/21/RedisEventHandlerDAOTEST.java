import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    @Mock
    JedisProxy jedisProxy;

    @Mock
    RedisProperties properties;

    @Mock
    ConductorProperties conductorProperties;

    @Mock
    ObjectMapper objectMapper;

    RedisEventHandlerDAO redisEventHandlerDAO;

    EventHandler eventHandlerTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
        eventHandlerTest = new EventHandler();
        eventHandlerTest.setName("TestHandler");
    }

    @Test
    public void addEventHandlerTest() {
        redisEventHandlerDAO.addEventHandler(eventHandlerTest);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NullPointerException.class)
    public void addEventHandlerNullTest() {
        redisEventHandlerDAO.addEventHandler(null);
    }

    @Test(expected = ConflictException.class)
    public void addEventHandlerConflictTest() {
        when(jedisProxy.hget(any(), any())).thenReturn("value");
        redisEventHandlerDAO.addEventHandler(eventHandlerTest);
    }
}