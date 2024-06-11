package com.oceanbase.odc.metadb.flow.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity.Builder;

import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepositoryImpl;

/**
 * Test class for {@link UserTaskInstanceRepository}
 */
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @InjectMocks
    private UserTaskInstanceRepositoryImpl userTaskInstanceRepositoryImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests the findByStatus method with valid status
     */
    @Test
    public void testFindByStatus() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity);
        when(userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.APPROVAL)).thenReturn(expectedList);

        // When
        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepositoryImpl.findByStatus(FlowNodeStatus.APPROVAL);

        // Then
        assertEquals(expectedList, actualList);
        verify(userTaskInstanceRepositoryMock, times(1)).findByStatus(FlowNodeStatus.APPROVAL);
    }

    /**
     * Tests the findByStatus method with null status
     */
    @Test(expected = NullPointerException.class)
    public void testFindByStatusNullStatus() {
        // Given
        when(userTaskInstanceRepositoryMock.findByStatus(null)).thenReturn(Collections.emptyList());

        // When
        userTaskInstanceRepositoryImpl.findByStatus(null);
    }

    /**
     * Tests the findByCandidateUserIdOrRoleIds method with valid userId and roleIds
     */
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity);
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L)))
                .thenReturn(expectedList);

        // When
        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepositoryImpl
                .findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));

        // Then
        assertEquals(expectedList, actualList);
        verify(userTaskInstanceRepositoryMock, times(1)).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));
    }

    /**
     * Tests the findByCandidateUserIdOrResourceRoleIdentifier method with valid userId and resourceRoleIdentifier
     */
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity);
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role")))
                .thenReturn(expectedList);

        // When
        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepositoryImpl
                .findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role"));

        // Then
        assertEquals(expectedList, actualList);
        verify(userTaskInstanceRepositoryMock, times(1))
                .findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role"));
    }

    /**
     * Tests the findByResourceRoleIdentifierIn method with valid resourceRoleIdentifier
     */
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity);
        when(userTaskInstanceRepositoryMock.findByResourceRoleIdentifierIn(Arrays.asList("role")))
                .thenReturn(expectedList);

        // When
        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepositoryImpl.findByResourceRoleIdentifierIn(
                Arrays.asList("role"));

        // Then
        assertEquals(expectedList, actualList);
        verify(userTaskInstanceRepositoryMock, times(1))
                .findByResourceRoleIdentifierIn(Arrays.asList("role"));
    }

    /**
     * Tests the findByRoleId method with valid roleId
     */
    @Test
    public void testFindByRoleId() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity);
        when(userTaskInstanceRepositoryMock.findByRoleId(1L)).thenReturn(expectedList);

        // When
        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepositoryImpl.findByRoleId(1L);

        // Then
        assertEquals(expectedList, actualList);
        verify(userTaskInstanceRepositoryMock, times(1)).findByRoleId(1L);
    }

    /**
     * Tests the findByCandidateUserId method with valid userId
     */
    @Test
    public void testFindByCandidateUserId() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity);
        when(userTaskInstanceRepositoryMock.findByCandidateUserId(1L)).thenReturn(expectedList);

        // When
        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepositoryImpl.findByCandidateUserId(1L);

        // Then
        assertEquals(expectedList, actualList);
        verify(userTaskInstanceRepositoryMock, times(1)).findByCandidateUserId(1L);
    }

    /**
     * Tests the update method with valid parameters
     */
    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        when(userTaskInstanceRepositoryMock.save(userTaskInstanceEntity)).thenReturn(userTaskInstanceEntity);

        // When
        UserTaskInstanceEntity updatedEntity = userTaskInstanceRepositoryImpl.update(userTaskInstanceEntity);

        // Then
        assertEquals(userTaskInstanceEntity, updatedEntity);
        verify(userTaskInstanceRepositoryMock, times(1)).save(userTaskInstanceEntity);
    }

    /**
     * Tests the updateUserTaskIdById method with valid id and userTaskId
     */
    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        when(userTaskInstanceRepositoryMock.save(any(UserTaskInstanceEntity.class))).thenAnswer(i -> i.getTarget());

        // When
        userTaskInstanceRepositoryImpl.updateUserTaskIdById(1L, "userTaskId");

        // Then
        verify(userTaskInstanceRepositoryMock, times(1))
                .save(argThat(entity -> entity.getUserTaskId().equals("userTaskId") && entity.getId().equals(1L)));
    }

    /**
     * Tests the updateStatusById method with valid id and status
     */
    @Test
    public void testUpdateStatusById() {
        // Given
        when(userTaskInstanceRepositoryMock.save(any(UserTaskInstanceEntity.class))).thenAnswer(i -> i.getTarget());

        // When
        userTaskInstanceRepositoryImpl.updateStatusById(1L, FlowNodeStatus.APPROVED);

        // Then
        verify(userTaskInstanceRepositoryMock, times(1))
                .save(argThat(entity -> entity.getStatus().equals(FlowNodeStatus.APPROVED) && entity.getId().equals(1L)));
    }

    /**
     * Tests the deleteByFlowInstanceId method with valid flowInstanceId
     */
    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        when(userTaskInstanceRepositoryMock.findAll()).thenReturn(Arrays.asList(userTaskInstanceEntity));

        // When
        userTaskInstanceRepositoryImpl.deleteByFlowInstanceId(1L);

        // Then
        verify(userTaskInstanceRepositoryMock, times(1)).deleteByFlowInstanceId(1L);
        verify(userTaskInstanceRepositoryMock, times(1)).findAll();
        verifyZeroInteractions(userTaskInstanceRepositoryMock);
    }

    /**
     * Tests the findByInstanceTypeAndActivityId method with valid instanceType, activityId, and flowInstanceId
     */
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        when(userTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        // When
        Optional<UserTaskInstanceEntity> actualOptional = userTaskInstanceRepositoryImpl
                .findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);

        // Then
        assertTrue(actualOptional.isPresent());
        assertEquals(userTaskInstanceEntity, actualOptional.get());
        verify(userTaskInstanceRepositoryMock, times(1))
                .findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);
    }

    /**
     * Tests the findByInstanceTypeAndName method with valid instanceType, name, and flowInstanceId
     */
    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        when(userTaskInstanceRepositoryMock.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        // When
        Optional<UserTaskInstanceEntity> actualOptional = userTaskInstanceRepositoryImpl
                .findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);

        // Then
        assertTrue(actualOptional.isPresent());
        assertEquals(userTaskInstanceEntity, actualOptional.get());
        verify(userTaskInstanceRepositoryMock, times(1))
                .findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);
    }

    /**
     * Tests the findConfirmById method with valid id
     */
    @Test
    public void testFindConfirmById() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        when(userTaskInstanceRepositoryMock.findByConfirmById(1L)).thenReturn(true);

        // When
        boolean actual = userTaskInstanceRepositoryImpl.findConfirmById(1L);

        // Then
        assertTrue(actual);
        verify(userTaskInstanceRepositoryMock, times(1)).findByConfirmById(1L);
    }

    /**
     * Tests the findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid flowInstanceIds and status
     */
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity.Builder().build();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity);
        when(userTaskInstanceRepositoryMock.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED))
                .thenReturn(expectedList);

        // When
        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepositoryImpl
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);

        // Then
        assertEquals(expectedList, actualList);
        verify(userTaskInstanceRepositoryMock, times(1))
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
    }
}