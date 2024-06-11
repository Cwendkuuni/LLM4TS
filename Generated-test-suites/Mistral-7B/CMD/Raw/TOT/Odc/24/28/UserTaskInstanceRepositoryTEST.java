package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByStatus(eq(FlowNodeStatus.APPROVED)))
                .thenReturn(Arrays.asList(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        assertEquals(2, result.size());
        verify(userTaskInstanceRepository, times(1)).findByStatus(eq(FlowNodeStatus.APPROVED));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity3 = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(eq(1L), anyCollection()))
                .thenReturn(Arrays.asList(entity1, entity2));
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(eq(2L), anyCollection()))
                .thenReturn(Collections.singletonList(entity3));

        // When
        List<UserTaskInstanceEntity> result1 = userTaskInstanceService.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        List<UserTaskInstanceEntity> result2 = userTaskInstanceService.findByCandidateUserIdOrRoleIds(2L, Arrays.asList(1L, 2L));

        // Then
        assertEquals(2, result1.size());
        assertEquals(1, result2.size());
        verify(userTaskInstanceRepository, times(2)).findByCandidateUserIdOrRoleIds(anyLong(), anyCollection());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        // Given
        doNothing().when(userTaskInstanceRepository).update(entity);

        // When
        int result = userTaskInstanceService.update(entity);

        // Then
        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).update(entity);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(FlowNodeType.APPROVAL), eq("activityId"), eq(1L)))
                .thenReturn(Optional.of(entity));

        // When
        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(eq(FlowNodeType.APPROVAL), eq("activityId"), eq(1L));
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findConfirmById(eq(1L)))
                .thenReturn(true);

        // When
        boolean result = userTaskInstanceService.findConfirmById(1L);

        // Then
        assertTrue(result);
        verify(userTaskInstanceRepository, times(1)).findConfirmById(eq(1L));
    }
}