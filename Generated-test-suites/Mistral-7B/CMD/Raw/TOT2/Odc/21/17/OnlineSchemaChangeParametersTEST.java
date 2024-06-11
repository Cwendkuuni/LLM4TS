import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

 @Test
 public void testOnlineSchemaChangeParametersWithValidInput() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))");
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
 onlineSchemaChangeParameters.setSwapTableType(SwapTableType.ASYNC);
 onlineSchemaChangeParameters.setDelimiter(";");
 onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());

 assertNotNull(onlineSchemaChangeParameters);
 }

 @Test
 public void testIsContinueOnErrorWithValidInput() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

 assertTrue(onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithInvalidSqlContent() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 onlineSchemaChangeParameters.setSqlContent("Invalid SQL Content;");
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
 onlineSchemaChangeParameters.setSwapTableType(SwapTableType.ASYNC);
 onlineSchemaChangeParameters.setDelimiter(";");
 onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());

 OnlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithNullConnectionConfig() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))";
 onlineSchemaChangeChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 onlineSchemaChangeParameters.setSqlContent(sqlContent);
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
 onlineSchemaChangeParameters.setSwapTableType(SwapTableType.ASYNC);
 onlineSchemaChangeParameters.setDelimiter(";");
 onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
 ConnectionConfig connectionConfig = null;
 String schema = "test_schema";

 OnlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithEmptySchema() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))");
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
 onlineSchemaChangeParameters.setSwapTableType(SwapTableType.ASYNC);
 onlineSchemaChangeParameters.setDelimiter(";");
 onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
 String schema = "";

 OnlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 }

}