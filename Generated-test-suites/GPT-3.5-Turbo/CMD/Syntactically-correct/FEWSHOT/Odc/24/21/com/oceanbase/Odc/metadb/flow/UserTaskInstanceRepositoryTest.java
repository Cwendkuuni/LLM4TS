package com.oceanbase.odc.metadb.flow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private JpaRepository<UserTaskInstanceEntity, Long> jpaRepository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setStatus(status);
        
        Example<UserTaskInstanceEntity> example = Example.of(entity);

        Mockito.when(jpaRepository.findAll(example)).thenReturn(Collections.singletonList(entity));
        
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);
        
        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);

        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds))
                .thenReturn(Collections.emptyList());

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(0, result.size());
    }

    // Add more test cases for other methods in UserTaskInstanceRepository

}