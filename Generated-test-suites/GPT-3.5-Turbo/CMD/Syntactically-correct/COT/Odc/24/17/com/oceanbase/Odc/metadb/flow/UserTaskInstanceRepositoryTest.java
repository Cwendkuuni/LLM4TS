import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> instances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS);
        assertNotNull(instances);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> instances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertNotNull(instances);
    }

    // Add more test cases for other methods...

    // Edge cases and exception handling scenarios
    @Test
    public void testFindByCandidateUserIdOrRoleIds_EmptyInput() {
        List<UserTaskInstanceEntity> instances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Collections.emptyList());
        assertNotNull(instances);
        assertTrue(instances.isEmpty());
    }

    @Test
    public void testUpdate_Status() {
        int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
        assertEquals(1, rowsAffected);
    }

    // Add more test cases for edge cases and exception handling...

}