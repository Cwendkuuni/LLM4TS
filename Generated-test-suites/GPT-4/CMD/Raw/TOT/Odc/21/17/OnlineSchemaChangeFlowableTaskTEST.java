import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;



/**
 * Tests for {@link com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask}.
 */
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
    @Mock
    private DelegateExecution delegateExecution;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        Mockito.when(onlineSchemaChangeFlowableTask.start(1L, null, delegateExecution)).thenReturn(scheduleEntity);
        ScheduleEntity result = onlineSchemaChangeFlowableTask.start(1L, null, delegateExecution);
        Assertions.assertNotNull(result, "Expected not null result");
        Assertions.assertEquals(scheduleEntity, result, "Expected and actual results should be same");
    }
}

