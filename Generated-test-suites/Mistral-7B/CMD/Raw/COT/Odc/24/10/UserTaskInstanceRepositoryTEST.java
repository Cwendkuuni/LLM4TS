package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Before
 public void setup() {
 userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);
 }

 @Test
 public void testFindByStatus() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 BDDMockito.given(userTaskInstanceRepository.findByStatus(Mockito.eq(FlowNodeStatus.APPROVED)))
 .willReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

 // Then
 assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 BDDMockito.given(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.any()))
 .willReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));

 // Then
 assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 @Test
 public void testFindByResourceRoleIdentifier() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 BDDMockito.given(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Mockito.eq(Arrays.asList("roleIdentifier"))))
 .willReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("roleIdentifier"));

 // Then
 assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 @Test
 public void testFindByRoleId() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 BDDMockito.given(userTaskInstanceRepository.findByRoleId(Mockito.anyLong()))
 .willReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(1L);

 // Then
 assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 BDDMockito.given(userTaskInstanceRepository.findByCandidateUserId(Mockito.anyLong()))
 .willReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(1L);

 // Then
 assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 @Test
 public void testUpdate() {
 // Given
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 UserTaskInstanceEntity updatedUserTaskInstanceEntity = new UserTaskInstanceEntity();
 BDDMockito.given(userTaskInstanceRepository.save(Mockito.eq(userTaskInstanceEntity)))
 .willReturn(updatedUserTaskInstanceEntity);

 // When
 int actualNumberOfRowsUpdated = userTaskInstanceRepository.update(userTaskInstanceEntity);

 // Then
 assertEquals(1, actualNumberOfRowsUpdated);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 long id = 1L;
 String userTaskId = "userTaskId";
 BDDMockito.given(userTaskInstanceRepository.save(Mockito.any()))
 .willReturn(new UserTaskInstanceEntity());
 BDDMockito.given(userTaskInstanceRepository.findById(Mockito.eq(id)))
 .willReturn(Optional.of(new UserTaskInstanceEntity()));

 // When
 int actualNumberOfRowsUpdated = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

 // Then
 assertEquals(1, actualNumberOfRowsUpdated);
 }

 @Test
 public void testUpdateStatusById() {
 // Given
 long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 BDDMockito.given(userTaskInstanceRepository.save(Mockito.any()))
 .willReturn(new UserTaskInstanceEntity());
 BDDMockito.given(userTaskInstanceRepository.findById(Mockito.eq(id)))
 .willReturn(Optional.of(new UserTaskInstanceEntity()));

 // When
 int actualNumberOfRowsUpdated = userTaskInstanceRepository.updateStatusById(id, status);

 // Then
 assertEquals(1, actualNumberOfRowsUpdated);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 long instanceId = 1L;
 BDDMockito.given(userTaskInstanceRepository.findAll())
 .willReturn(Collections.emptyList());
 BDDMockito.given(userTaskInstanceRepository.count())
 .willReturn(0L);

 // When
 int actualNumberOfRowsDeleted = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

 // Then
 assertEquals(0, actualNumberOfRowsDeleted);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 BDDMockito.given(userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.eq(FlowNodeType.APPROVAL), "activityId", 1L))
 .willReturn(Optional.of(userTaskInstanceEntity));

 // When
 Optional<UserTaskInstanceEntity> actualOptionalUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);

 // Then
 assertNotNull(actualOptionalUserTaskInstanceEntity.get());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 BDDMockito.given(userTaskInstanceRepository.findByInstanceTypeAndName(Mockito.eq(FlowNodeType.APPROVAL), "name", 1L))
 .willReturn(Optional.of(userTaskInstanceEntity));

 // When
 Optional<UserTaskInstanceEntity> actualOptionalUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);

 // Then
 assertNotNull(actualOptionalUserTaskInstanceEntity.get());
 }

 @Test
 public void testFindConfirmById() {
 // Given
 Long id = 1L;
 BDDMockito.given(userTaskInstanceRepository.findByConfirmById(id))
 .willReturn(true);

 // When
 Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);

 // Then
 assertEquals(true, actualConfirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 Long flowInstanceId = 1L;
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 BDDMockito.given(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Mockito.eq(flowInstanceId), Mockito.eq(FlowNodeStatus.APPROVED)))
 .willReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, FlowNodeStatus.APPROVED);

 // Then
 assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

}