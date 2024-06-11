import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

public class UserTaskInstanceRepositoryTest {

 @InjectMocks
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 // Test for findByStatus method
 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 // Test for findByCandidateUserIdOrRoleIds method with valid input
 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 // Test for findByCandidateUserIdOrResourceRoleIdentifier method with valid input
 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("admin");
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 // Test for findByResourceRoleIdentifierIn method with valid input
 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("admin");
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 // Test for findByRoleId method with valid input
 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 // Test for findByCandidateUserId method with valid input
 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 // Test for update method with valid input
 @Test
 public void testUpdate() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setUserTaskId("userTaskId");
 userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
 userTaskInstanceEntity.setApproved(true);
 userTaskInstanceEntity.setOperatorId(1L);
 userTaskInstanceEntity.setComment("comment");
 userTaskInstanceEntity.setExpireIntervalSeconds(10);
 userTaskInstanceEntity.setExternalFlowInstanceId(1L);

 int rowsAffected = userTaskInstanceRepository.update(userTaskInstanceEntity);
 Assert.assertEquals(1, rowsAffected);
 }

 // Test for updateUserTaskIdById method with valid input
 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "userTaskId";

 int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 Assert.assertEquals(1, rowsAffected);
 }

 // Test for updateStatusById method with valid input
 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
 Assert.assertEquals(1, rowsAffected);
 }

 // Test for deleteByFlowInstanceId method with valid input
 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;

 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 Assert.assertEquals(1, rowsAffected);
 }

 // Test for findByInstanceTypeAndActivityId method with valid input
 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.DISPLAY;
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assert.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 // Test for findByInstanceTypeAndName method with valid input
 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.DISPLAY;
 String name = "name";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assert.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 // Test for findConfirmById method with valid input
 @Test
 public void testFindConfirmById() {
 Long id = 1L;

 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 Assert.assertEquals(Boolean.FALSE, confirm); // Assuming confirm is false for this id
 }

 // Test for findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid input
 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

}