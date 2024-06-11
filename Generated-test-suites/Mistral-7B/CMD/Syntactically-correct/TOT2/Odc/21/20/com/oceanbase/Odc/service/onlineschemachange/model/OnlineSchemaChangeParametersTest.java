import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        OnlineSchemaChangeParameters onlineSchemaChangeParameters1 = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters1.setSqlType(sqlType);
        onlineSchemaChangeParameters1.setErrorStrategy(TaskErrorStrategy.STOP);

        assertFalse(onlineSchemaChangeParameters1.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setDelimiter(";");
        String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT; ALTER TABLE test_table DROP COLUMN old_column;";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        when(connectionConfig.getDialectType()).thenReturn(SqlUtils.DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
        expectedTaskParameter1.setDatabaseName("test_database");
        expectedTaskParameter1.setOriginTableName("test_table");
        expectedTaskParameter1.setNewTableName("test_table_new");
        expectedTaskParameter1.setSqlsToBeExecuted(Arrays.asList(
                "ALTER TABLE test_table ADD COLUMN new_column INT",
                "ALTER TABLE test_table DROP COLUMN old_column"
        ));
        expectedSubTaskParameters.add(expectedTaskParameter1);

        List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(expectedSubTaskParameters, actualSubTaskParameters);
    }
}