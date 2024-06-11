package com.oceanbase.odc.metadb.flow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void shouldFindUserTaskInstanceByStatus(){
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        // assuming that there is a UserTaskInstance with APPROVED status in your test db.
        
        List<UserTaskInstanceEntity> found = userTaskInstanceRepository.findByStatus(status);
        assertThat(found).isNotEmpty();
    }
}