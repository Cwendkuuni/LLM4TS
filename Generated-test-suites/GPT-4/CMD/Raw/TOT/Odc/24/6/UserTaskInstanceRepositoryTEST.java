import org.junit.*;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTaskInstanceRepositoryTest {
    @Autowired
    private UserTaskInstanceRepository repository;

    @Test
    public void findByStatusReturnsCorrectEntity() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setStatus(FlowNodeStatus.InProgress);
        repository.save(entity);

        List<UserTaskInstanceEntity> found = repository.findByStatus(FlowNodeStatus.InProgress);

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getStatus()).isEqualTo(FlowNodeStatus.InProgress);
    }
}