package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;
import org.mockito.InjectMocks;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        // add some dummy data to expectedList
        assertEquals(expectedList, userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        // add some dummy data to expectedList
        assertEquals(expectedList, userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds));
    }

    // Add more test cases for other methods

}