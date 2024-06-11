import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private JpaRepository<UserTaskInstanceEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<UserTaskInstanceEntity> jpaSpecificationExecutor;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.ACTIVE;

        List<UserTaskInstanceEntity> expectedEntities = new ArrayList<>();
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setStatus(status);
        expectedEntities.add(entity);

        when(userTaskInstanceRepository.findByStatus(eq(status))).thenReturn(expectedEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);

        assertEquals(expectedEntities, result);
    }

    // Add more test cases for other public methods in UserTaskInstanceRepository

    // Test edge cases and exception handling scenarios

}