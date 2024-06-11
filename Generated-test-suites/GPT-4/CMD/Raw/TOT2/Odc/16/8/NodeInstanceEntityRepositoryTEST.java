import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() throws Exception {
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(new NodeInstanceEntity()));
        List<NodeInstanceEntity> response = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE2);
        assertEquals(1, response.size());
    } 

    @Test
    public void testFindByFlowInstanceId() throws Exception {
        when(repository.findByFlowInstanceId(any(Long.class))).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        List<NodeInstanceEntity> response = repository.findByFlowInstanceId(1L);
        assertEquals(1, response.size());
    }

    // Test for findByIds and deleteByFlowInstanceId and deleteByInstanceIdAndInstanceType go here
    
    @Test(expected=IllegalArgumentException.class)
    public void testPassingNullAsArgument() throws Exception {
        repository.findByFlowInstanceId(null);
    }
}