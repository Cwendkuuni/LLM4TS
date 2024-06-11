package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.RUNNING;
        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(),new UserTaskInstanceEntity());
        when(entityManager.createQuery("from UserTaskInstanceEntity ut where ut.status=:status", UserTaskInstanceEntity.class)
            .setParameter("status", status.name())
            .getResultList()).thenReturn(entities);

        List<UserTaskInstanceEntity> result = repository.findByStatus(status);
        assertNotNull(result);
        assertEquals(entities.size(), result.size());

        //TODO: Repeat this test case pattern to cover all the find/update/delete method in UserTaskInstanceRepository interface.
    }
}