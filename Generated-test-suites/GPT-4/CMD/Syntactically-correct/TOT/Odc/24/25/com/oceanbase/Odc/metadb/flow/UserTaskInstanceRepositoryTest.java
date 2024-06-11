package com.oceanbase.odc.metadb.flow;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserTaskInstanceEntity mockEntity;

    @Mock
    FlowNodeStatus mockStatus;

    @Mock
    List<UserTaskInstanceEntity> mockList;

    @Test
    public void testFindByStatus() {
        userTaskInstanceRepository.findByStatus(mockStatus);
        verify(userTaskInstanceRepository, times(1)).findByStatus(mockStatus);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L, 3L, 4L));
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L, 3L, 4L));
    }

    // Additional tests would follow this pattern...

}
