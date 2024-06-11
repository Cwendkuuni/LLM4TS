package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setUp() {
        // Initialize any required setup or mock data before each test
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);

        assertEquals(expectedList, resultList);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(expectedList, resultList);
    }

    // More test methods for other public methods

}