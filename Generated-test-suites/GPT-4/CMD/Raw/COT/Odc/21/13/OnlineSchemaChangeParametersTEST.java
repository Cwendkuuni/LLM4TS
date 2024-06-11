
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.core.shared.model.TableIdentity;

import java.util.List;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameter;
    private ConnectionConfig connectionConfig;
    
    @Before
    public void setup(){
    	parameter = new OnlineSchemaChangeParameters();
    	connectionConfig = new ConnectionConfig();
    }
    
    @Test
    public void testIsContinueOnError() {
        parameter.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertEquals(true, parameter.isContinueOnError());
    }
    
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
    	parameter.setSqlContent("CREATE table test (id INT);");
    	parameter.setSqlType(OnlineSchemaChangeSqlType.CREATE);
    	List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = parameter.generateSubTaskParameters(connectionConfig,"testSchema");
    	
    	//Assuming only one sql subQuery is there and parameters are as set above
    	OnlineSchemaChangeScheduleTaskParameters subTask = subTaskParams.get(0);
    	TableIdentity tableId = new TableIdentity("test",subTask.getOriginTableName());
    	
    	Assert.assertEquals("test",subTask.getDatabaseName());
    	Assert.assertEquals("test",subTask.getOriginTableName());
    	Assert.assertEquals("CREATE table test (id INT);",subTask.getSqlsToBeExecuted().get(0));
    	Assert.assertEquals(tableId, subTask.getSwapTableName());
    }
}
