package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private JpaRepository<UserTaskInstanceEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<UserTaskInstanceEntity> jpaSpecificationExecutor;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.ACTIVE;

        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.add(new UserTaskInstanceEntity());
        expectedList.add(new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByStatus(status);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);

        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.add(new UserTaskInstanceEntity());
        expectedList.add(new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
    }

    // Add more test cases for other methods as needed...

}