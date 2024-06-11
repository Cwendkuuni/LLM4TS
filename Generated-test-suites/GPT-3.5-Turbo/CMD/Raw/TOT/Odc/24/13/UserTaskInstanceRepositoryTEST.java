import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {
    
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private JpaRepository<UserTaskInstanceEntity, Long> jpaRepository;
    
    @Mock
    private JpaSpecificationExecutor<UserTaskInstanceEntity> jpaSpecificationExecutor;
    
    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expected);
        
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(status);
        
        assertEquals(expected, actual);
    }

    // Add test cases for other methods...

}