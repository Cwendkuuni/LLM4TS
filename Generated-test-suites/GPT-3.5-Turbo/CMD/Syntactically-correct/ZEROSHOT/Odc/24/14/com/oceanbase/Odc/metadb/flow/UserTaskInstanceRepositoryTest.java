import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity testEntity;

    @Before
    public void setUp() {
        testEntity = new UserTaskInstanceEntity();
        testEntity.setId(1L);
        testEntity.setUserTaskId("testTask");
        testEntity.setStatus(FlowNodeStatus.PENDING);
        testEntity.setApproved(false);
        testEntity.setOperatorId(123L);
        testEntity.setComment("test comment");
        testEntity.setExpireIntervalSeconds(60);
        testEntity.setExternalFlowInstanceId("testInstance");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedEntities = new ArrayList<>();
        expectedEntities.add(testEntity);
        
        when(userRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(expectedEntities);
        
        List<UserTaskInstanceEntity> actualEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedEntities = new ArrayList<>();
        expectedEntities.add(testEntity);
        
        when(userRepository.findByCandidateUserIdOrRoleIds(eq(123L), anyCollectionOf(Long.class))).thenReturn(expectedEntities);
        
        List<UserTaskInstanceEntity> actualEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(123L, Arrays.asList(1L, 2L));
        
        assertEquals(expectedEntities, actualEntities);
    }

    // Add more test cases for other methods...

}