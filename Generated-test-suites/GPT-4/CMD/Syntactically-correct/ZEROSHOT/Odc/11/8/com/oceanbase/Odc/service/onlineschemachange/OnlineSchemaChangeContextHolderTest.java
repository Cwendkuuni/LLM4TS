package com.oceanbase.odc.service.onlineschemachange;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Mock
    SecurityContextUtils mockSecurityContextUtils;

    @Test
    public void testTraceLongParameters() {
        //Sample values for the method parameters
        Long userId = 1L;
        Long taskId = 1L;
        Long organizationId = 1L;

        //Call to the static method
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        //Assertions
        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), userId.toString());
        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID), taskId.toString());
        assertEquals(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID), organizationId.toString());
    }

    @Test
    public void testTraceStringParameters() {
        //Sample values for the method parameters
        String userId = "1";
        String taskId = "1";
        String organizationId = "1";

        //Call to the static method
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        //Assertions
        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), userId);
        assertEquals(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID), taskId);
        assertEquals(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID), organizationId);
    }

    @Test
    public void testRetrace() {
        Map<String, String> contextMap = new HashMap<>();
        contextMap.put("key1", "value1");
        contextMap.put("key2", "value2");

        //Call to the static method
        OnlineSchemaChangeContextHolder.retrace(contextMap);

        //Assertions
        assertEquals(OnlineSchemaChangeContextHolder.get("key1"), "value1");
        assertEquals(OnlineSchemaChangeContextHolder.get("key2"), "value2");
    }

    @Test
    public void testClear() {
        //Sample values for the method parameters
        Long userId = 1L;
        Long taskId = 1L;
        Long organizationId = 1L;

        //Call to the static method
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        OnlineSchemaChangeContextHolder.clear();

        //Assertions
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        //Adding a key in the map
        OnlineSchemaChangeContextHolder.retrace(new HashMap<String, String>() {{
            put("key", "value");
        }});

        String result = OnlineSchemaChangeContextHolder.get("key");
        assertEquals(result, "value");
    }
}