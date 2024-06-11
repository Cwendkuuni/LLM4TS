import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @BeforeEach
 void setUp() {
 userTaskInstanceRepository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
 }

 @AfterEach
 void tearDown() {
 // Clean up tests data if necessary
 }

 @Test
 void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
 assertNotNull(userTaskInstanceEntities);
 assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 void testFindByCandidateUserIdOrRoleIds() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
 assertNotNull(userTaskInstanceEntities);
 assertEquals(1, userTaskInstanceEntities.size());

 List<UserTaskInstanceEntity> userTaskInstanceEntities2 = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Arrays.asList("role1"));
 assertNotNull(userTaskInstanceEntities2);
 assertEquals(1, userTaskInstanceEntities2.size());
 }

 @Test
 void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, null);
 assertNotNull(userTaskInstanceEntities);
 assertEquals(1, userTaskInstanceEntities.size());

 List<UserTaskInstanceEntity> userTaskInstanceEntities2 = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, Arrays.asList("role1"));
 assertNotNull(userTaskInstanceEntities2);
 assertEquals(1, userTaskInstanceEntities2.size());
 }

 @Test
 void testFindByResourceRoleIdentifierIn() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));
 assertNotNull(userTaskInstanceEntities);
 assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 void testFindByRoleId() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(1L);
 assertNotNull(userTaskInstanceEntities);
 assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(1L);
 assertNotNull(userTaskInstanceEntities);
 assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 void testUpdate() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setUserTaskId("taskId");
 userTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);
 userTaskInstanceEntity.setApproved(true);
 userTaskInstanceEntity.setOperatorId("operatorId");
 userTaskInstanceEntity.setComment("comment");
 userTaskInstanceEntity.setExpireIntervalSeconds(10L);
 userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");

 UserTaskInstanceEntity updatedUserTaskInstanceEntity = userTaskInstanceRepository.update(userTaskInstanceEntity);
 assertEquals(userTaskInstanceEntity, updatedUserTaskInstanceEntity);
 }

 @Test
 void testUpdateUserTaskIdById() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setUserTaskId("oldUserTaskId");
 userTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);

 userTaskInstanceRepository.updateUserTaskIdById(1L, "newUserTaskId");
 UserTaskInstanceEntity updatedUserTaskInstanceEntity = userTaskInstanceRepository.findByCandidateUserId(1L);
 assertEquals("newUserTaskId", updatedUserTaskInstanceEntity.getUserTaskId());
 }

 @Test
 void testUpdateStatusById() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setUserTaskId("userTaskId");
 userTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);
 userTaskInstanceEntity.setApproved(false);
 userTaskInstanceEntity.setOperatorId("operatorId");
 userTaskInstanceEntity.setComment("comment");
 userTaskInstanceEntity.setExpireIntervalSeconds(10L);
 userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");

 userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
 UserTaskInstanceEntity updatedUserTaskInstanceEntity = userTaskInstanceRepository.findByCandidateUserId(1L);
 assertEquals(FlowNodeStatus.APPROVED, updatedUserTaskInstanceEntity.getStatus());
 }

 @Test
 void testDeleteByFlowInstanceId() {
 userTaskInstanceRepository.deleteByFlowInstanceId(1L);
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByFlowInstanceId(1L);
 assertEmpty(userTaskInstanceEntities);
 }

 @Test
 void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);
 assertTrue(userTaskInstanceEntityOptional.isPresent());
 UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
 assertEquals(FlowNodeType.START, userTaskInstanceEntity.getInstanceType());
 assertEquals("activityId", userTaskInstanceEntity.getActivityId());
 assertEquals(1L, userTaskInstanceEntity.getFlowInstanceId());
 }

 @Test
 void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.END, "name", 1L);
 assertTrue(userTaskInstanceEntityOptional.isPresent());
 UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
 assertEquals(FlowNodeType.END, userTaskInstanceEntity.getInstanceType());
 assertEquals("name", userTaskInstanceEntity.getName());
 assertEquals(1L, userTaskInstanceEntity.getFlowInstanceId());
 }

 @Test
 void testFindConfirmById() {
 Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
 assertFalse(confirm);
 }

 @Test
 void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.PENDING);
 assertNotNull(userTaskInstanceEntities);
 assertEquals(2, userTaskInstanceEntities.size());
 }
}