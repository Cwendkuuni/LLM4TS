import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.UserTaskInstanceService;

@ExtendWith(MockitoExtension.class)
public class UserTaskInstanceRepositoryTest {

 @Autowired
 @InjectMocks
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Autowired
 private UserTaskInstanceService userTaskInstanceService;

 @Test
 public void testFindByStatus() {
 // Given
 List<UserTaskInstanceEntity> expectedStatusList = Mockito.mock(List.class);
 Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.any(FlowNodeStatus.class))).thenReturn(expectedStatusList);

 // When & Then
 List<UserTaskInstanceEntity> actualStatusList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertEquals(expectedStatusList, actualStatusList);
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 List<UserTaskInstanceEntity> expectedList = Mockito.mock(List.class);
 Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.any())).thenReturn(expectedList);

 // When & Then
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
 Assertions.assertEquals(expectedList, actualList);
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 // Given
 List<UserTaskInstanceEntity> expectedList = Mockito.mock(List.class);
 Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(Mockito.anyLong(), Mockito.any()))
 .thenReturn(expectedList);

 // When & Then
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, null);
 Assertions.assertEquals(expectedList, actualList);
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 // Given
 List<UserTaskInstanceEntity> expectedList = Mockito.mock(List.class);
 Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Mockito.any()))
 .thenReturn(expectedList);

 // When & Then
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(null);
 Assertions.assertEquals(expectedList, actualList);
 }

 @Test
 public void testFindByRoleId() {
 // Given
 List<UserTaskInstanceEntity> expectedList = Mockito.mock(List.class);
 Mockito.when(userTaskInstanceRepository.findByRoleId(Mockito.anyLong()))
 .thenReturn(expectedList);

 // When & Then
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByRoleId(1L);
 Assertions.assertEquals(expectedList, actualList);
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 List<UserTaskInstanceEntity> expectedList = Mockito.mock(List.class);
 Mockito.when(userTaskInstanceRepository.findByCandidateUserId(Mockito.anyLong()))
 .thenReturn(expectedList);

 // When & Then
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserId(1L);
 Assertions.assertEquals(expectedList, actualList);
 }

 @Test
 @Rollback(value = false)
 public void testUpdate() {
 // Given
 UserTaskInstanceEntity entity = Mockito.mock(UserTaskInstanceEntity.class);
 Mockito.when(userTaskInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(entity));

 // When
 UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
 updatedEntity.setId(1L);
 updatedEntity.setUserTaskId("testUserTaskId");
 updatedEntity.setStatus(FlowNodeStatus.APPROVED);
 updatedEntity.setApproved(true);
 updatedEntity.setOperatorId(1L);
 updatedEntity.setComment("test comment");
 updatedEntity.setExpireIntervalSeconds(120);
 updatedEntity.setExternalFlowInstanceId("testExternalFlowInstanceId");

 userTaskInstanceRepository.saveAndFlush(updatedEntity);

 // Then
 UserTaskInstanceEntity actualEntity = userTaskInstanceRepository.findById(1L).get();
 Assertions.assertEquals(updatedEntity.getUserTaskId(), actualEntity.getUserTaskId());
 Assertions.assertEquals(updatedEntity.getStatus(), actualEntity.getStatus());
 Assertions.assertEquals(updatedEntity.isApproved(), actualEntity.isApproved());
 Assertions.assertEquals(updatedEntity.getOperatorId(), actualEntity.getOperatorId());
 Assertions.assertEquals(updatedEntity.getComment(), actualEntity.getComment());
 Assertions.assertEquals(updatedEntity.getExpireIntervalSeconds(), actualEntity.getExpireIntervalSeconds());
 Assertions.assertEquals(updatedEntity.getExternalFlowInstanceId(), actualEntity.getExternalFlowInstanceId());
 }

 @Test
 @Rollback(value = false)
 public void testUpdateUserTaskIdById() {
 // Given
 UserTaskInstanceEntity entity = Mockito.mock(UserTaskInstanceEntity.class);
 Mockito.when(userTaskInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(entity));

 // When
 userTaskInstanceRepository.updateUserTaskIdById(1L, "testUserTaskId");

 // Then
 UserTaskInstanceEntity actualEntity = userTaskInstanceRepository.findById(1L).get();
 Assertions.assertEquals("testUserTaskId", actualEntity.getUserTaskId());
 }

 @Test
 @Rollback(value = false)
 public void testUpdateStatusById() {
 // Given
 UserTaskInstanceEntity entity = Mockito.mock(UserTaskInstanceEntity.class);
 Mockito.when(userTaskInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(entity));

 // When
 userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);

 // Then
 UserTaskInstanceEntity actualEntity = userTaskInstanceRepository.findById(1L).get();
 Assertions.assertEquals(FlowNodeStatus.APPROVED, actualEntity.getStatus());
 }

 @Test
 @Rollback(value = false)
 public void testDeleteByFlowInstanceId() {
 // Given
 UserTaskInstanceEntity entity = Mockito.mock(UserTaskInstanceEntity.class);
 Mockito.when(userTaskInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(entity));

 // When
 userTaskInstanceRepository.deleteByFlowInstanceId(1L);

 // Then
 Assertions.assertFalse(userTaskInstanceRepository.existsById(1L));
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 UserTaskInstanceEntity expectedEntity = Mockito.mock(UserTaskInstanceEntity.class);
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.any(FlowNodeType.class), Mockito.anyString(), Mockito.anyLong()))
 .thenReturn(Optional.of(expectedEntity));

 // When & Then
 Optional<UserTaskInstanceEntity> actualEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "testActivityId", 1L);
 Assertions.assertTrue(actualEntityOptional.isPresent());
 Assertions.assertEquals(expectedEntity, actualEntityOptional.get());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 UserTaskInstanceEntity expectedEntity = Mockito.mock(UserTaskInstanceEntity.class);
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(Mockito.any(FlowNodeType.class), Mockito.anyString(), Mockito.anyLong()))
 .thenReturn(Optional.of(expectedEntity));

 // When & Then
 Optional<UserTaskInstanceEntity> actualEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.END, "testName", 1L);
 Assertions.assertTrue(actualEntityOptional.isPresent());
 Assertions.assertEquals(expectedEntity, actualEntityOptional.get());
 }

 @Test
 public void testFindConfirmById() {
 // Given
 boolean expectedConfirm = true;
 Mockito.when(userTaskInstanceRepository.findConfirmById(Mockito.anyLong())).thenReturn(expectedConfirm);

 // When & Then
 boolean actualConfirm = userTaskInstanceRepository.findConfirmById(1L);
 Assertions.assertEquals(expectedConfirm, actualConfirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 List<UserTaskInstanceEntity> expectedList = Mockito.mock(List.class);
 Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Mockito.any(), Mockito.any()))
 .thenReturn(expectedList);

 // When & Then
 List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(1L, FlowNodeStatus.APPROVED);
 Assertions.assertEquals(expectedList, actualList);
 }
}