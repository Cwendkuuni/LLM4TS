import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

public class UserTaskInstanceRepositoryTest {

 @MockBean
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Autowired
 private UserTaskInstanceRepository testSubject;

 @Test
 public void testFindByStatus() {
 UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
 expected.setId(1L);
 expected.setStatus(FlowNodeStatus.APPROVED);
 Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(java.util.Arrays.asList(expected));

 List<UserTaskInstanceEntity> actual = testSubject.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertEquals(expected, actual.get(0));
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, java.util.Arrays.asList(1L, 2L)))
 .thenReturn(java.util.Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity()));

 List<UserTaskInstanceEntity> actual = testSubject.findByCandidateUserIdOrRoleIds(1L, java.util.Arrays.asList(1L, 2L));
 Assertions.assertEquals(2, actual.size());
 }

 @Test
 public void testFindByResourceRoleIdentifier() {
 Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(java.util.Arrays.asList("role1", "role2")))
 .thenReturn(java.util.Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity()));

 List<UserTaskInstanceEntity> actual = testSubject.findByResourceRoleIdentifierIn(java.util.Arrays.asList("role1", "role2"));
 Assertions.assertEquals(2, actual.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 Mockito.when(userTaskInstanceRepository.save(Mockito.eq(entity))).thenReturn(entity);

 UserTaskInstanceEntity updated = testSubject.update(entity);
 Assertions.assertEquals(entity, updated);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "userTask1";
 Mockito.when(userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId)).thenReturn(1);

 int actual = testSubject.updateUserTaskIdById(id, userTaskId);
 Assertions.assertEquals(1, actual);
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 Mockito.when(userTaskInstanceRepository.updateStatusById(id, status)).thenReturn(1);

 int actual = testSubject.updateStatusById(id, status);
 Assertions.assertEquals(1, actual);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 Mockito.when(userTaskInstanceRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

 int actual = testSubject.deleteByFlowInstanceId(instanceId);
 Assertions.assertEquals(1, actual);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.TASK;
 String activityId = "activity1";
 UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
 expected.setId(1L);
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
 .thenReturn(Optional.of(expected));

 Optional<UserTaskInstanceEntity> actual = testSubject.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assertions.assertEquals(expected, actual.get());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.TASK;
 String name = "name1";
 UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
 expected.setId(1L);
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
 .thenReturn(Optional.of(expected));

 Optional<UserTaskInstanceEntity> actual = testSubject.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assertions.assertEquals(expected, actual.get());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(true);

 boolean actual = testSubject.findConfirmById(id);
 Assertions.assertTrue(actual);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Long flowInstanceId = 1L;
 List<UserTaskInstanceEntity> expected = java.util.Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(java.util.Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED))
 .thenReturn(expected);

 List<UserTaskInstanceEntity> actual = testSubject.findApprovalInstanceIdByFlowInstanceIdAndStatus(java.util.Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testFindAll() {
 Pageable pageable = PageRequest.of(0, 10);
 List<UserTaskInstanceEntity> expected = java.util.Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Page<UserTaskInstanceEntity> page = new PageImpl<>(expected);
 Mockito.when(userTaskInstanceRepository.findAll(pageable)).thenReturn(page);

 Page<UserTaskInstanceEntity> actual = testSubject.findAll(pageable);
 Assertions.assertEquals(expected, actual.getContent());
 }

 @Test
 public void testFindAllByFlowInstanceId() {
 Long flowInstanceId = 1L;
 List<UserTaskInstanceEntity> expected = java.util.Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findAllByFlowInstanceId(flowInstanceId)).thenReturn(expected);

 List<UserTaskInstanceEntity> actual = testSubject.findAllByFlowInstanceId(flowInstanceId);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testFindAllByFlowInstanceIdAndStatus() {
 Long flowInstanceId = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 List<UserTaskInstanceEntity> expected = java.util.Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findAllByFlowInstanceIdAndStatus(flowInstanceId, status)).thenReturn(expected);

 List<UserTaskInstanceEntity> actual = testSubject.findAllByFlowInstanceIdAndStatus(flowInstanceId, status);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testFindAllByUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> expected = java.util.Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findAllByUserId(userId)).thenReturn(expected);

 List<UserTaskInstanceEntity> actual = testSubject.findAllByUserId(userId);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testFindAllByUserIdAndRoleId() {
 Long userId = 1L;
 Long roleId = 1L;
 List<UserTaskInstanceEntity> expected = java.util.Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findAllByUserIdAndRoleId(userId, roleId)).thenReturn(expected);

 List<UserTaskInstanceEntity> actual = testSubject.findAllByUserIdAndRoleId(userId, roleId);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testFindAllByUserIdAndResourceRoleIdentifier() {
 Long userId = 1L;
 String resourceRoleIdentifier = "role";
 List<UserTaskInstanceEntity> expected = java.util.Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findAllByUserIdAndResourceRoleIdentifier(userId, resourceRoleIdentifier))
 .thenReturn(expected);

 List<UserTaskInstanceEntity> actual = testSubject.findAllByUserIdAndResourceRoleIdentifier(userId, resourceRoleIdentifier);
 Assertions.assertEquals(expected, actual);
 }
}