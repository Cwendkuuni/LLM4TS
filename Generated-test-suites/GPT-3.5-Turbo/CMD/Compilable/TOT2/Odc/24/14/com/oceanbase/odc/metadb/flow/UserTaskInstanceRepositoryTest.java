package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = Collections.singletonList(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // Add test cases for other public methods following the same pattern

}