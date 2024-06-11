import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

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

    @Before
    public void setUp() {

    }

    @Test
    public void testFindByStatus() {
        // Given
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setStatus(status);
        List<UserTaskInstanceEntity> expectedList = Collections.singletonList(entity);
        
        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedList);
        
        // When
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByStatus(status);
        
        // Then
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(status, resultList.get(0).getStatus());
    }

    // Other test cases can be added for the remaining methods in a similar manner

}