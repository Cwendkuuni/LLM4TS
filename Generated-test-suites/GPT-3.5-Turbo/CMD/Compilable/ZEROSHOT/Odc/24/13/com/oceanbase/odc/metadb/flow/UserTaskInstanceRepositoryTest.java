package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    private UserTaskInstanceEntity entity;

    @Before
    public void setUp() {
        entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("123");
        entity.setStatus(FlowNodeStatus.PENDING);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(entity);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.PENDING);

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(entity.getId(), resultList.get(0).getId());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(entity);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L))).thenReturn(expectedList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(entity.getId(), resultList.get(0).getId());
    }

    // Add test cases for other methods in UserTaskInstanceRepository

}