import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.add(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertNotNull(actualList);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(2L, 3L);

        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.add(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertNotNull(actualList);
        assertEquals(expectedList, actualList);
    }

    // Add more test cases for other methods...

}