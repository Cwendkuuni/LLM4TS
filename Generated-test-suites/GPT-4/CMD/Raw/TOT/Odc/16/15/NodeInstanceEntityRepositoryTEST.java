
import static org.junit.Assert.*;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NodeInstanceEntityRepositoryTest{
    
    @InjectMocks
    private NodeInstanceEntityRepository repository;

    @Mock
    private NodeInstanceEntity nodeInstance;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
         //setup your test
         //when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(arguments)).thenReturn(expected_result);
         //assert
    }
   
    @Test
    public void testFindByFlowInstanceId() {
        //setup your test
        //when(repository.findByFlowInstanceId(id)).thenReturn(expected_result);
        //assert
    }

    @Test
    public void testFindByIds() {
         //setup your test
         //when(repository.findByIds(ids)).thenReturn(expected_result);
         //assert
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        //setup your test
        //when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(arguments)).thenReturn(expected_result);
        //assert
        // this test should check that the entity has been effectively removed
    }
   
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        //setup your test
        //when(repository.deleteByInstanceIdAndInstanceType(arguments)).thenReturn(expected_result);
        //assert
        //this test should check that the entity has been effectively removed
    }
}
