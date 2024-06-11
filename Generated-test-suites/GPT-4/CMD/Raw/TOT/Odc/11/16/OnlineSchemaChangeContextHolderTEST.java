import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void clearBefore() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTrace_LongParameters() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), is("1"));
        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), is("2"));
        assertThat(MDC.get(OdcConstants.ORGANIZATION_ID), is("3"));
    }

    @Test
    public void testTrace_StringParameters() {
        OnlineSchemaChangeContextHolder.trace("4", "5", "6");
        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), is("4"));
        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), is("5"));
        assertThat(MDC.get(OdcConstants.ORGANIZATION_ID), is("6"));
    }

    @Test
    public void testTrace_StringParameters_NullValues() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), is(nullValue()));
        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), is(nullValue()));
        assertThat(MDC.get(OdcConstants.ORGANIZATION_ID), is(nullValue()));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key", "value");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertThat(MDC.get("key"), is("value"));
    }

    @Test
    public void testClear() {
        MDC.put("key", "value");
        OnlineSchemaChangeContextHolder.clear();
        assertThat(MDC.get("key"), is(nullValue()));
    }

    @Test
    public void testGet() {
        MDC.put("key", "value");
        assertThat(OnlineSchemaChangeContextHolder.get("key"), is("value"));
    }

}