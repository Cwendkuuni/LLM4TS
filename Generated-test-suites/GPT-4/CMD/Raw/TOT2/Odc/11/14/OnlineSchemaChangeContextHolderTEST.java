import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Mock
    private MDC mdcMocked;

    @Before
    public void setup() {
        // you could setup initial configurations or mocks here
    }

    @Test
    public void trace_shouldPutMetaDataAndSetCurrentUser() {
        Long userId = 1L;
        Long taskId = 1L;
        Long organizationId = 1L;

        // Call the method to test
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        // Verify if MDC.put was called with right parameters
        verify(mdcMocked).put(eq(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), eq(userId+""));
        verify(mdcMocked).put(eq(OnlineSchemaChangeContextHolder.TASK_ID), eq(taskId+""));
        verify(mdcMocked).put(eq(OdcConstants.ORGANIZATION_ID), eq(organizationId+""));

        // Verify if SecurityContextUtils.getCurrentUser was called with right parameters
        verify(SecurityContextUtils).setCurrentUser(userId, organizationId, null);
    }

    @Test
    public void retrace_shouldSetContextMap() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");

        // Call the method to test
        OnlineSchemaChangeContextHolder.retrace(context);

        // Verify if MDC.setContextMap was called with right parameters
        verify(mdcMocked).setContextMap(context);
    }

    @Test
    public void clear_shouldClearMDC() {
        // Call the method to test
        OnlineSchemaChangeContextHolder.clear();

        // Verify if MDC.clear was called
        verify(mdcMocked).clear();
    }

    @Test
    public void get_shouldGetTheValueFromMDC() {
        String key = "key1";
        String value = "value1";
        when(mdcMocked.get(key)).thenReturn(value);

        // Call the method to test
        String responseValue = OnlineSchemaChangeContextHolder.get(key);

        // verify the return value
        assertEquals(value, responseValue);
    }

}