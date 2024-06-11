import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Autowired
 public void setUserTaskInstanceRepository(UserTaskInstanceRepository userTaskInstanceRepository) {
 this.userTaskInstanceRepository = userTaskInstanceRepository;
 }

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = List.of("role1", "role2");
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifiers = List.of("role1", "role2");
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
 Assert.assertNotNull(userTaskInstanceEntities);
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setUserTaskId("taskId");
 entity.setStatus(FlowNodeStatus.APPROVED);
 entity.setApproved(true);
 entity.setOperatorId(1L);
 entity.setComment("comment");
 entity.setExpireIntervalSeconds(60);
 entity.setExternalFlowInstanceId("externalId");

 int updatedRows = userTaskInstanceRepository.update(entity);
 Assert.assertEquals(1, updatedRows);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "taskId";
 int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 Assert.assertEquals(1, updatedRows);
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 int updatedRows = userTaskInstanceRepository.updateStatusById(id, status);
 Assert.assertEquals(1, updatedRows);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 Assert.assertEquals(1, deletedRows);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.ACTIVITY;
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> userTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assert.assertTrue(userTaskInstanceEntity.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.ACTIVITY;
 String name = "name";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> userTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assert.assertTrue(userTaskInstanceEntity.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 Assert.assertNull(confirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = List.of(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 Assert.assertNotNull(userTaskInstanceEntities);
 }
}