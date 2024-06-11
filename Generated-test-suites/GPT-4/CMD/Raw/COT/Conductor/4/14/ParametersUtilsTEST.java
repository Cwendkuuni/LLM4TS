import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {
    ParametersUtils utils = new ParametersUtils(new ObjectMapper());
    WorkflowModel model = new WorkflowModel();
    TaskDef taskDef = new TaskDef();
    
    @Test
    public void testGetTaskInputNullParams() {
        assertNull(utils.getTaskInput(null, model, taskDef, "taskId"));
    }

    @Test
    public void testGetTaskInputNonNullParamsButEmptyMap() {
        Map<String, Object> map = new HashMap<>();
        assertEquals(map, utils.getTaskInput(map, model, taskDef, "taskId"));
    }

    @Test
    public void testGetTaskInputV2NullParams() {
        assertNull(utils.getTaskInputV2(null, model, "taskId", taskDef));
    }
    
    @Test
    public void testReplacement() {
        assertEquals("{}", utils.replace(new HashMap<>(), "{}"));
    }

    @Test
    public void testNullReplaceParamString() {
        assertNull(utils.replace((String) null));
    }

    @Test
    public void testGetWorkflowInputNullWorkflowDef() {
        assertNull(utils.getWorkflowInput(null, new HashMap<>()));
    }

    @Test
    public void testGetWorkflowInputNonNullWorkflowDefButNullMap() {
        assertNull(utils.getWorkflowInput(new WorkflowDef(), null));
    }
    
    @Test
    public void testGetWorkflowInputNonNullWorkflowDefButEmptyMap() {
        WorkflowDef def = new WorkflowDef();
        assertEquals(new HashMap<>(), utils.getWorkflowInput(def, new HashMap<>()));
    }
}