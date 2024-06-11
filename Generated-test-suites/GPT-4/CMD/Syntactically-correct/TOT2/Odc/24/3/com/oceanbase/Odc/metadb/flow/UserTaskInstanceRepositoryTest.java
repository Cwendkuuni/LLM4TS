package com.oceanbase.odc.metadb.flow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    private TestEntityManager entityManager;

    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.NEW;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setStatus(status);
        entityManager.persist(entity);

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(status);

        assertThat(entities).isNotEmpty();
        assertThat(entities.get(0).getStatus()).isEqualByComparingTo(status);
    }
    // other test methods..
}