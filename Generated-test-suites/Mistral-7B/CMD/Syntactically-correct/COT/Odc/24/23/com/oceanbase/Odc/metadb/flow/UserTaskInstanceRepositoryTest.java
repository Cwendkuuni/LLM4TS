package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntityBuilder;

import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;

public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;
 private UserTaskInstanceEntity exampleUserTaskInstance;

 @Before
 public void setup() {
 userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
 exampleUserTaskInstance = new UserTaskInstanceEntityBuilder().build();
 }

 @Test
 public void testFindByStatus() {
 // Given
 when(userTaskInstanceRepository.findByStatus(eq(FlowNodeStatus.APPROVED))).thenReturn(Arrays.asList(exampleUserTaskInstance));

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

 // Then
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals(exampleUserTaskInstance, result.get(0));
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(any(Long.class), any(Collection.class)))
 .thenReturn(Arrays.asList(exampleUserTaskInstance));

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));

 // Then
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals(exampleUserTaskInstance, result.get(0));
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 // Given
 when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(any(Long.class), any(Collection.class)))
 .thenReturn(Arrays.asList(exampleUserTaskInstance));

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
 Arrays.asList("roleIdentifier"));

 // Then
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals(exampleUserTaskInstance, result.get(0));
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 // Given
 when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(any(Collection.class)))
 .thenReturn(Arrays.asList(exampleUserTaskInstance));

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("roleIdentifier"));

 // Then
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals(exampleUserTaskInstance, result.get(0));
 }

 @Test
 public void testFindByRoleId() {
 // Given
 when(userTaskInstanceRepository.findByRoleId(any(Long.class)))
 .thenReturn(Arrays.asList(exampleUserTaskInstance));

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);

 // Then
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals(exampleUserTaskInstance, result.get(0));
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 when(userTaskInstanceRepository.findByCandidateUserId(any(Long.class)))
 .thenReturn(Arrays.asList(exampleUserTaskInstance));

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);

 // Then
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals(exampleUserTaskInstance, result.get(0));
 }

 @Test
 public void testUpdate() {
 // Given
 when(userTaskInstanceRepository.save(any(UserTaskInstanceEntity.class))).thenReturn(exampleUserTaskInstance);

 // When
 UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntityBuilder().build();
 int result = userTaskInstanceRepository.update(updatedEntity);

 // Then
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullEntity() {
 // Given
 when(userTaskInstanceRepository.save(any(UserTaskInstanceEntity.class))).thenReturn(exampleUserTaskInstance);

 // When & Then
 assertThrows(IllegalArgumentException.class, () -> userTaskInstanceRepository.update(null));
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 when(userTaskInstanceRepository.save(any(UserTaskInstanceEntity.class))).thenReturn(exampleUserTaskInstance);

 // When
 int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");

 // Then
 assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 // Given
 when(userTaskInstanceRepository.save(any(UserTaskInstanceEntity.class))).thenReturn(exampleUserTaskInstance);

 // When
 int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);

 // Then
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 when(userTaskInstanceRepository.save(any(UserTaskInstanceEntity.class))).thenReturn(exampleUserTaskInstance);

 // When
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);

 // Then
 assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), any(String.class), any(Long.class)))
 .thenReturn(Optional.of(exampleUserTaskInstance));

 // When
 Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APP_APPROVAL, "activityId", 1L);

 // Then
 assertTrue(result.isPresent());
 assertEquals(exampleUserTaskInstance, result.get());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class), any(String.class), any(Long.class)))
 .thenReturn(Optional.of(exampleUserTaskInstance));

 // When
 Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APP_APPROVAL, "name", 1L);

 // Then
 assertTrue(result.isPresent());
 assertEquals(exampleUserTaskInstance, result.get());
 }

 @Test
 public void testFindConfirmById() {
 // Given
 when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(true);

 // When
 boolean result = userTaskInstanceRepository.findConfirmById(1L);

 // Then
 assertTrue(result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 when(userTaskInstanceRepository.findByStatus(eq(FlowNodeStatus.APPROVED)))
 .thenReturn(Arrays.asList(exampleUserTaskInstance));

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.APPROVED);

 // Then
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals(exampleUserTaskInstance, result.get(0));
 }

}