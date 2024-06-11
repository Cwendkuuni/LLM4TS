import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connConfig;
    
    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        connConfig = new ConnectionConfig();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWhenExceptionOccurs() {
        parameters.setSqlContent("sqlContent");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        SqlUtils sqlUtils = Mockito.mock(SqlUtils.class);
        ArrayList<String> list = new ArrayList<>(Arrays.asList("sql1", "sql2"));
        
        Mockito.doReturn(list).when(sqlUtils).split(connConfig.getDialectType(), parameters.getSqlContent(), parameters.getDelimiter());
        
        parameters.generateSubTaskParameters(connConfig, "schema");
    }

    @Test
    public void testGenerateSubTaskParametersWithNullParameter() {
        ArrayList<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(null, null);
        
        assertTrue(result.isEmpty());
    } 
}