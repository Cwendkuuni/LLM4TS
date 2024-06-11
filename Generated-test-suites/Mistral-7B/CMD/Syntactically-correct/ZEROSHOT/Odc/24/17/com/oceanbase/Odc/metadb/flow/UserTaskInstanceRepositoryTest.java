import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Before
 public void setUp() {
 userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
 }

 @Test
 public void testFindByStatus() {
 // Given
 List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED))
 .thenReturn(expectedList);

 // When
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

 // Then
 assertEquals("Size should be equal", expectedList.size(), actualList.size());
 for (int i = 0; i < expectedList.size(); i++) {
 assertEquals("Item " + i + " should be equal", expectedList.get(i), actualList.get(i));
 }
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, Arrays.asList(1L, 2L)))
 .thenReturn(expectedList);

 // When
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, Arrays.asList(1L, 2L));

 // Then
 assertEquals("Size should be equal", expectedList.size(), actualList.size());
 for (int i = 0; i < expectedList.size(); i++) {
 assertEquals("Item " + i + " should be equal", expectedList.get(i), actualList.get(i));
 }
 }

 @Test
 public void testFindByResourceRoleIdentifier() {
 // Given
 String resourceRoleIdentifier = "resourceRoleIdentifier";
 List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList(resourceRoleIdentifier)))
 .thenReturn(expectedList);

 // When
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList(resourceRoleIdentifier));

 // Then
 assertEquals("Size should be equal", expectedList.size(), actualList.size());
 for (int i = 0; i < expectedList.size(); i++) {
 assertEquals("Item " + i + " should be equal", expectedList.get(i), actualList.get(i));
 }
 }

 @Test
 public void testFindByRoleId() {
 // Given
 Long roleId = 1L;
 List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 when(userTaskInstanceRepository.findByRoleId(roleId))
 .thenReturn(expectedList);

 // When
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByRoleId(roleId);

 // Then
 assertEquals("Size should be equal", expectedList.size(), actualList.size());
 for (int i = 0; i < expectedList.size(); i++) {
 assertEquals("Item " + i + " should be equal", expectedList.get(i), actualList.get(i));
 }
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 Long userId = 1L;
 List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 when(userTaskInstanceRepository.findByCandidateUserId(userId))
 .thenReturn(expectedList);

 // When
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserId(userId);

 // Then
 assertEquals("Size should be equal", expectedList.size(), actualList.size());
 for (int i = 0; i < expectedList.size(); i++) {
 assertEquals("Item " + i + " should be equal", expectedList.get(i), actualList.get(i));
 }
 }

 @Test
 public void testUpdate() {
 // Given
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 when(userTaskInstanceRepository.save(entity))
 .thenReturn(entity);

 // When
 UserTaskInstanceEntity updatedEntity = userTaskInstanceRepository.save(entity);

 // Then
 assertSame("Should be the same instance", entity, updatedEntity);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 Long id = 1L;
 String userTaskId = "userTaskId";
 doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

 // When
 int affectedRows = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

 // Then
 assertEquals("Number of affected rows should be equal", 1, affectedRows);
 }

 @Test
 public void testUpdateStatusById() {
 // Given
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 doNothing().when(userTaskInstanceRepository).updateStatusById(id, status);

 // When
 int affectedRows = userTaskInstanceRepository.updateStatusById(id, status);

 // Then
 assertEquals("Number of affected rows should be equal", 1, affectedRows);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;
 doNothing().when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

 // When
 int affectedRows = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

 // Then
 assertEquals("Number of affected rows should be equal", 1, affectedRows);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 UserTaskInstanceEntity expectedEntity = new UserTaskInstanceEntity();
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, "activityId", flowInstanceId))
 .thenReturn(Optional.of(expectedEntity));

 // When
 Optional<UserTaskInstanceEntity> actualEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, "activityId", flowInstanceId);

 // Then
 assertTrue("Optional should not be empty", actualEntity.isPresent());
 assertEquals("Entities should be equal", expectedEntity, actualEntity.get());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name";
 UserTaskInstanceEntity expectedEntity = new UserTaskInstanceEntity();
 when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
 .thenReturn(Optional.of(expectedEntity));

 // When
 Optional<UserTaskInstanceEntity> actualEntity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

 // Then
 assertTrue("Optional should not be empty", actualEntity.isPresent());
 assertEquals("Entities should be equal", expectedEntity, actualEntity.get());
 }

 @Test
 public void testFindConfirmById() {
 // Given
 Long id = 1L;
 boolean expectedConfirm = true;
 when(userTaskInstanceRepository.findConfirmById(id))
 .thenReturn(expectedConfirm);

 // When
 boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);

 // Then
 assertEquals("Confirm should be equal", expectedConfirm, actualConfirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 Long flowInstanceId = 1L;
 List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
 expectedList.add(new UserTaskInstanceEntity());
 expectedList.add(new UserTaskInstanceEntity());
 when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(flowInstanceId), FlowNodeStatus.APPROVED))
 .thenReturn(expectedList);

 // When
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(flowInstanceId), FlowNodeStatus.APPROVED);

 // Then
 assertEquals("Size should be equal", expectedList.size(), actualList.size());
 for (int i = 0; i < expectedList.size(); i++) {
 assertEquals("Item " + i + " should be equal", expectedList.get(i), actualList.get(i));
 }
 }
}