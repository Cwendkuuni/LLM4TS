package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {
    @InjectMocks
    private UserTaskInstanceRepository mockUserTaskInstanceRepository;

    @Mock
    private UserTaskInstanceEntity mockUserTaskInstanceEntity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByStatusTest() {
        when(mockUserTaskInstanceRepository.findByStatus(any(FlowNodeStatus.class)))
            .thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> entities = mockUserTaskInstanceRepository.findByStatus(FlowNodeStatus.NEW);
        assertTrue(entities.size() == 0);
    }

    @Test
    public void findByCandidateUserIdOrRoleIdsTest() {
        when(mockUserTaskInstanceRepository.findByCandidateUserIdOrRoleIds(any(Long.class), any(Collection.class)))
            .thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> entities = mockUserTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>());
        assertTrue(entities.size() == 0);
    }

    @Test
    public void findByCandidateUserIdOrResourceRoleIdentifierTest() {
        when(mockUserTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(any(Long.class), any(Collection.class)))
            .thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> entities = mockUserTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>());
        assertTrue(entities.size() == 0);
    }

    @Test
    public void findByResourceRoleIdentifierInTest() {
        when(mockUserTaskInstanceRepository.findByResourceRoleIdentifierIn(any(Collection.class)))
            .thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> entities = mockUserTaskInstanceRepository.findByResourceRoleIdentifierIn(new ArrayList<>());
        assertTrue(entities.size() == 0);
    }

    // Additional test cases for rest of the methods go here
}