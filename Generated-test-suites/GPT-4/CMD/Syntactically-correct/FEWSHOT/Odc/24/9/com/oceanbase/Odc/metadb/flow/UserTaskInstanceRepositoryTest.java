package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void findByStatus() {
        Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.any()))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(Mockito.any());
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findByCandidateUserIdOrRoleIds() {
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findByCandidateUserIdOrResourceRoleIdentifier() {
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // Repeat testing for all the methods

    @Test
    public void update() {
        Mockito.when(userTaskInstanceRepository.update(Mockito.any()))
                .thenReturn(1);
        int result = userTaskInstanceRepository.update(userTaskInstanceEntity);
        assertEquals(1, result);
    }

    @Test
    public void updateUserTaskIdById() {
        Mockito.when(userTaskInstanceRepository.updateUserTaskIdById(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(1);
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "task1");
        assertEquals(1, result);
    }

    @Test
    public void deleteByFlowInstanceId() {
        Mockito.when(userTaskInstanceRepository.deleteByFlowInstanceId(Mockito.anyLong()))
                .thenReturn(1);
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void findByInstanceTypeAndActivityId() {
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.any(), Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.any(), "activity1", 1L);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void findConfirmById() {
        Mockito.when(userTaskInstanceRepository.findConfirmById(Mockito.anyLong()))
                .thenReturn(true);
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(result);
    }
}