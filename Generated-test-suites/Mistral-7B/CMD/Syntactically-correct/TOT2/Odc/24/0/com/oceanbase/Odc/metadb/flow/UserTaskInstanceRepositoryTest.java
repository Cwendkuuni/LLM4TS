import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.specification.UserTaskInstanceSpecification;

@SpringBootTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @MockBean
 private UserTaskInstanceSpecification userTaskInstanceSpecification;

 @Test
 public void testFindByStatus() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
 userTaskInstanceRepository.save(userTaskInstanceEntity);

 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertNotNull(actual);
 Assertions.assertEquals(1, actual.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = List.of(
 new UserTaskInstanceEntity(),
 new UserTaskInstanceEntity(),
 new UserTaskInstanceEntity().setUserId(userId)
 );
 userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, null);
 Assertions.assertNotNull(actual);
 Assertions.assertEquals(1, actual.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 String resourceRoleIdentifier = "admin";
 List<UserTaskInstanceEntity> userTaskInstanceEntities = List.of(
 new UserTaskInstanceEntity().setResourceRoleIdentifier(resourceRoleIdentifier),
 new UserTaskInstanceEntity(),
 new UserTaskInstanceEntity().setResourceRoleIdentifier(resourceRoleIdentifier)
 );
 userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList(resourceRoleIdentifier));
 Assertions.assertNotNull(actual);
 Assertions.assertEquals(2, actual.size());
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = List.of(
 new UserTaskInstanceEntity().setRoleId(roleId),
 new UserTaskInstanceEntity(),
 new UserTaskInstanceEntity().setRoleId(roleId)
 );
 userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(roleId);
 Assertions.assertNotNull(actual);
 Assertions.assertEquals(2, actual.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setUserTaskId("123");
 userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
 userTaskInstanceEntity.setApproved(true);
 userTaskInstanceEntity.setOperatorId("admin");
 userTaskInstanceEntity.setComment("comment");
 userTaskInstanceEntity.setExpireIntervalSeconds(3600);
 userTaskInstanceEntity.setExternalFlowInstanceId("external-123");

 UserTaskInstanceEntity updatedUserTaskInstanceEntity = new UserTaskInstanceEntity();
 updatedUserTaskInstanceEntity.setId(1L);
 updatedUserTaskInstanceEntity.setUserTaskId("456");
 updatedUserTaskInstanceEntity.setStatus(FlowNodeStatus.REJECTED);
 updatedUserTaskInstanceEntity.setApproved(false);
 updatedUserTaskInstanceEntity.setOperatorId("user");
 updatedUserTaskInstanceEntity.setComment("updated comment");
 updatedUserTaskInstanceEntity.setExpireIntervalSeconds(7200);
 updatedUserTaskInstanceEntity.setExternalFlowInstanceId("external-456");

 Mockito.when(userTaskInstanceRepository.save(Mockito.any(UserTaskInstanceEntity.class)))
 .thenReturn(updatedUserTaskInstanceEntity);

 UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(1L).orElseThrow();
 userTaskInstanceRepository.update(userTaskInstanceEntity);

 Assertions.assertEquals("456", actual.getUserTaskId());
 Assertions.assertEquals(FlowNodeStatus.REJECTED, actual.getStatus());
 Assertions.assertFalse(actual.isApproved());
 Assertions.assertEquals("user", actual.getOperatorId());
 Assertions.assertEquals("updated comment", actual.getComment());
 Assertions.assertEquals(7200, actual.getExpireIntervalSeconds());
 Assertions.assertEquals("external-456", actual.getExternalFlowInstanceId());
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "123";

 Mockito.when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(new UserTaskInstanceEntity().setId(id)));
 Mockito.when(userTaskInstanceRepository.save(Mockito.any(UserTaskInstanceEntity.class)))
 .thenReturn(new UserTaskInstanceEntity().setId(id).setUserTaskId(userTaskId));

 userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

 UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).orElseThrow();
 Assertions.assertEquals(userTaskId, actual.getUserTaskId());
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 Mockito.when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(new UserTaskInstanceEntity().setId(id)));
 Mockito.when(userTaskInstanceRepository.save(Mockito.any(UserTaskInstanceEntity.class)))
 .thenReturn(new UserTaskInstanceEntity().setId(id).setStatus(status));

 userTaskInstanceRepository.updateStatusById(id, status);

 UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).orElseThrow();
 Assertions.assertEquals(status, actual.getStatus());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setFlowInstanceId(instanceId);
 userTaskInstanceRepository.save(userTaskInstanceEntity);

 int actual = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 Assertions.assertEquals(1, actual);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activity-1";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setInstanceType(instanceType);
 userTaskInstanceEntity.setActivityId(activityId);
 userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
 userTaskInstanceRepository.save(userTaskInstanceEntity);

 Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assertions.assertTrue(actual.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setInstanceType(instanceType);
 userTaskInstanceEntity.setName(name);
 userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
 userTaskInstanceRepository.save(userTaskInstanceEntity);

 Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assertions.assertTrue(actual.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean confirm = true;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(id);
 userTaskInstanceEntity.setWaitForConfirm(confirm);
 userTaskInstanceRepository.save(userTaskInstanceEntity);

 Boolean actual = userTaskInstanceRepository.findConfirmById(id);
 Assertions.assertEquals(confirm, actual);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Long flowInstanceId = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = List.of(
 new UserTaskInstanceEntity().setFlowInstanceId(flowInstanceId),
 new UserTaskInstanceEntity().setFlowInstanceId(flowInstanceId).setStatus(status),
 new UserTaskInstanceEntity().setFlowInstanceId(flowInstanceId).setStatus(FlowNodeStatus.REJECTED)
 );
 userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status);
 Assertions.assertNotNull(actual);
 Assertions.assertEquals(1, actual.size());
 }

 @Test
 public void testFindAll() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findAll(pageable);
 Assertions.assertNotNull(actual);
 Assertions.assertEquals(1, actual.getContent().size());
 }

 @Test
 public void testFindAllBySpecification() {
 Specification<UserTaskInstanceEntity> specification = userTaskInstanceSpecification.findByStatus(FlowNodeStatus.APPROVED);
 Page<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findAll(specification, pageable);
 Assertions.assertNotNull(actual);
 Assertions.assertEquals(1, actual.getContent().size());
 }
}