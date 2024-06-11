import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.TaskErrorStrategy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters createValidParameters() {
 return new OnlineSchemaChangeParameters()
 .setSqlType(OnlineSchemaChangeSqlType.ALTER)
 .setSqlContent("ALTER TABLE test_table ALTER COLUMN column_name TYPE new_type")
 .setErrorStrategy(TaskErrorStrategy.CONTINUE)
 .setLockTableTimeOutSeconds(5)
 .setSwapTableType(SwapTableType.EXCLUSIVE)
 .setDelimiter(";")
 .setLockUsers(new ArrayList<>())
 .setOriginTableCleanStrategy(OriginTableCleanStrategy.RETAIN);
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = createValidParameters();
 Assert.assertTrue(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters_validParameters() throws Exception {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = createValidParameters();
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 Assert.assertFalse(subTaskParameters.isEmpty());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParameters_exception() throws Exception {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, null);
 }

 @Test
 public void testGenerateSubTaskParameters_sqlTypeIsNull() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(null);
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(null, null);
 Assert.assertFalse(subTaskParameters.isEmpty());
 }

 @Test
 public void testGenerateSubTaskParameters_sqlContentIsNull() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlContent(null);
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(null, null);
 Assert.assertFalse(subTaskParameters.isEmpty());
 }

 @Test
 public void testGenerateSubTaskParameters_lockTableTimeOutSecondsIsNegative() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setLockTableTimeOutSeconds(-1);
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(null, null);
 Assert.assertFalse(subTaskParameters.isEmpty());
 }

 @Test
 public void testGenerateSubTaskParameters_swapTableTypeIsNull() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSwapTableType(null);
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(null, null);
 Assert.assertFalse(subTaskParameters.isEmpty());
 }

 @Test
 public void testGenerateSubTaskParameters_emptySqlContent() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("");
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(null, null);
 Assert.assertFalse(subTaskParameters.isEmpty());
 }

 @Test
 public void testGenerateSubTaskParameters_emptyLockUsers() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setLockUsers(new ArrayList<>());
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(null, null);
 Assert.assertFalse(subTaskParameters.isEmpty());
 }

}