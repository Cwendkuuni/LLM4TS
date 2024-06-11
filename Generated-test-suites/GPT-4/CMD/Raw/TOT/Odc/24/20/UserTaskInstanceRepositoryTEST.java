import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.annotation.Resource;

@DataJpaTest
@ExtendWith(MockitoExtension.class) // extend with Mockito
public class UserTaskInstanceRepositoryTest {
    @Resource
    private TestEntityManager testEntityManager;

    @Resource
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @BeforeEach
    public void setUp() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setStatus(FlowNodeStatus.PENDING);
        testEntityManager.persist(entity);

        entity = new UserTaskInstanceEntity();
        entity.setStatus(FlowNodeStatus.APPROVED);
        testEntityManager.persist(entity);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> tasks = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);

        assertThat(tasks).isNotEmpty();
        assertEquals(1, tasks.size());
        assertEquals(FlowNodeStatus.PENDING, tasks.get(0).getStatus());
    }
}