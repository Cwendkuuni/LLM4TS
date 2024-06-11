import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_validInput() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("sql1;sql2;sql3");
        parameters.setSqlType(OnlineSchemaChangeSqlType.DROP);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.REPLACE);

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expectedResult = new LinkedHashMap<>();
        TableIdentity tableIdentity1 = new TableIdentity("db1", "table1");
        OnlineSchemaChangeScheduleTaskParameters taskParameters1 = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(taskParameters1.getDatabaseName()).thenReturn("db1");
        when(taskParameters1.getOriginTableName()).thenReturn("table1");
        when(taskParameters1.getNewTableName()).thenReturn("new_table1");
        when(taskParameters1.getSqlsToBeExecuted()).thenReturn(new ArrayList<>());
        expectedResult.put(tableIdentity1, taskParameters1);

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("Exceptional_sql;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.DROP);

        PowerMockito.whenNew(SubTaskParameterFactory.class)
                .withArguments(ArgumentMatchers.eq(connectionConfig), ArgumentMatchers.eq(schema))
                .thenThrow(new RuntimeException("Exception in SubTaskParameterFactory."));

        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

}