package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void findByStatusShouldReturnUserTaskInstanceEntities() {
        FlowNodeStatus status = new FlowNodeStatus(/* init valid status here */);
        List<UserTaskInstanceEntity> list = new ArrayList<UserTaskInstanceEntity>(Arrays.asList(new UserTaskInstanceEntity(/* init valid entity here */), new UserTaskInstanceEntity(/* init another valid entity here */) ));

        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(list);

        List<UserTaskInstanceEntity> foundList = userTaskInstanceRepository.findByStatus(status);

        verify(userTaskInstanceRepository, times(1)).findByStatus(status);

        assertFalse(foundList.isEmpty());
        assertTrue(foundList.size() == 2);
        // further assertions to test the contents of the list, for example:
        // assertEquals(expectedEntity.getId(), foundList.get(0).getId());
    }
}