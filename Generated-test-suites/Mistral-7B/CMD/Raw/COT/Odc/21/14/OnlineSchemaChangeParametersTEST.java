import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.common.util.SqlUtils;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.NO_SWAP);
        parameters.setDelimiter(";");
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_ValidParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters parameter = taskParameters.get(0);
        assertEquals("CREATE TABLE test_table (id INT PRIMARY KEY)", parameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameter.getSqlType());
        assertNull(parameter.getNewTableName());
        assertFalse(parameter.isSwapTable());
        assertEquals(1, parameter.getSqlsToBeExecuted().size());
    }

    @Test
    public void testGenerateSubTaskParameters_InvalidSqlContent() {
        parameters.setSqlContent(null);
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(connectionConfig, schema));
    }

    @Test
    public void testGenerateSubTaskParameters_InvalidSqlType() {
        parameters.setSqlType(null);
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(connectionConfig, schema));
    }

    @Test
    public void testGenerateSubTaskParameters_InvalidLockUsers() {
        List<String> lockUsers = null;
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(connectionConfig, schema));

        lockUsers = new ArrayList<>();
        parameters.setLockUsers(lockUsers);
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(connectionConfig, schema));

        lockUsers.add("user1");
        parameters.setLockUsers(lockUsers);
        assertNull(parameters.getLockUsers());
    }

    @Test
    public void testGenerateSubTaskParameters_InvalidSwapTableType() {
        parameters.setSwapTableType(null);
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(connectionConfig, schema));
    }
}