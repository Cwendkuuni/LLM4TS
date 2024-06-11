package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Resource
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.USER);
        assertThat(nodeInstanceEntities).isNotEmpty();
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertThat(nodeInstanceEntities).isNotEmpty();
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));
        assertThat(nodeInstanceEntities.size()).isEqualTo(3);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int deleteCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertThat(deleteCount).isGreaterThan(0);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int deleteCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertThat(deleteCount).isGreaterThan(0);
    }
}