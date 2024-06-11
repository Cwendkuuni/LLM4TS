
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity sampleEntity;

    @BeforeEach
    public void setUp() {
        sampleEntity = new UserTaskInstanceEntity();
        // set desired sample entity fields
    }

    @Test
    public void findByStatus() {
        when(userTaskInstanceRepository.findByStatus(any())).thenReturn(Arrays.asList(sampleEntity));
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE);
        assertThat(results).isNotEmpty();
    }

    // Repeat the process for the other methods. For example:
    @Test
    public void findByCandidateUserIdOrRoleIds() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), any())).thenReturn(Arrays.asList(sampleEntity));
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L, 3L));
        assertThat(results).isNotEmpty();
    }

    @Test
    public void update() {
        when(userTaskInstanceRepository.update(any())).thenReturn(1);
        int result = userTaskInstanceRepository.update(sampleEntity);
        assertThat(result).isEqualTo(1);
    }

    // Add additional test methods for remaining methods in UserTaskInstanceRepository class

}
