package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

public class NodeInstanceEntityRepositoryTest {

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);
        expectedNodeInstanceEntities.add(nodeInstanceEntity1);

        NodeInstanceEntityRepository repository = new TestNodeInstanceEntityRepository();
        // When: Find nodeInstanceEntities by instanceId, instanceType and flowableElementType
        List<NodeInstanceEntity> nodeInstanceEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then: Verify the result
        assertTrue(nodeInstanceEntities.size() == 1);
        assertTrue(nodeInstanceEntities.contains(expectedNodeInstanceEntities.get(0)));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.PROCESS);
        expectedNodeInstanceEntities.add(nodeInstanceEntity1);

        NodeInstanceEntityRepository repository = new TestNodeInstanceEntityRepository();
        // When: Find nodeInstanceEntities by flowInstanceId
        List<NodeInstanceEntity> nodeInstanceEntities = repository.findByFlowInstanceId(flowInstanceId);

        // Then: Verify the result
        assertTrue(nodeInstanceEntities.size() == 1);
        assertTrue(nodeInstanceEntities.contains(expectedNodeInstanceEntities.get(0)));
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.PROCESS);
        expectedNodeInstanceEntities.add(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.PROCESS);
        expectedNodeInstanceEntities.add(nodeInstanceEntity2);

        NodeInstanceEntityRepository repository = new TestNodeInstanceEntityRepository();
        // When: Find nodeInstanceEntities by ids
        List<NodeInstanceEntity> nodeInstanceEntities = repository.findByIds(ids);

        // Then: Verify the result
        assertTrue(nodeInstanceEntities.size() == 2);
        assertTrue(nodeInstanceEntities.containsAll(expectedNodeInstanceEntities));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedNumOfDeletedEntities = 2;
        NodeInstanceEntityRepository repository = new TestNodeInstanceEntityRepository();
        // Given: Create some nodeInstanceEntities
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.PROCESS);
        repository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.PROCESS);
        repository.save(nodeInstanceEntity2);

        // When: Delete nodeInstanceEntities by flowInstanceId
        int numOfDeletedEntities = repository.deleteByFlowInstanceId(instanceId);

        // Then: Verify the result
        assertFalse(repository.findByFlowInstanceId(instanceId).isEmpty());
        assertTrue(numOfDeletedEntities == expectedNumOfDeletedEntities);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumOfDeletedEntities = 1;
        NodeInstanceEntityRepository repository = new TestNodeInstanceEntityRepository();
        // Given: Create a nodeInstanceEntity
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.PROCESS);
        repository.save(nodeInstanceEntity);

        // When: Delete nodeInstanceEntity by instanceId and instanceType
        int numOfDeletedEntities = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then: Verify the result
        assertNull(repository.findByInstanceIdAndInstanceType(instanceId, instanceType));
        assertTrue(numOfDeletedEntities == expectedNumOfDeletedEntities);
    }
}

class TestNodeInstanceEntityRepository implements NodeInstanceEntityRepository {
    private List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();

    @Override
    public NodeInstanceEntity save(NodeInstanceEntity nodeInstanceEntity) {
        nodeInstanceEntities.add(nodeInstanceEntity);
        return nodeInstanceEntity;
    }

    @Override
    public List<NodeInstanceEntity> findAll() {
        return nodeInstanceEntities;
    }

    @Override
    public NodeInstanceEntity findById(Long aLong) {
        return nodeInstanceEntities.stream()
                .filter(nodeInstanceEntity -> Objects.equals(nodeInstanceEntity.getId(), aLong))
                .findFirst()
                .orElse(null);
    }

    @Override
    public long count() {
        return nodeInstanceEntities.size();
    }

    @Override
    public boolean existsById(Long aLong) {
        return findById(aLong) != null;
    }

    @Override
    public List<NodeInstanceEntity> findAll(Sort sort) {
        return nodeInstanceEntities;
    }

    @Override
    public List<NodeInstanceEntity> findAll(Pageable pageable) {
        Page<NodeInstanceEntity> page = new PageImpl<>(nodeInstanceEntities, pageable, nodeInstanceEntities.size());
        return page.getContent();
    }

    @Override
    public <S extends NodeInstanceEntity> List<S> saveAll(Iterable<S> iterable) {
        List<NodeInstanceEntity> nodeInstanceEntitiesToSave = new ArrayList<>();
        iterable.forEach(nodeInstanceEntitiesToSave::add);
        this.nodeInstanceEntities.addAll(nodeInstanceEntitiesToSave);
        return nodeInstanceEntitiesToSave;
    }

    @Override
    public void deleteById(Long aLong) {
        nodeInstanceEntities.removeIf(nodeInstanceEntity -> Objects.equals(nodeInstanceEntity.getId(), aLong));
    }

    @Override
    public void deleteAll(Iterable<? extends NodeInstanceEntity> iterable) {
        nodeInstanceEntities.removeAll(iterable);
    }

    @Override
    public void deleteAll() {
        nodeInstanceEntities.clear();
    }

    @Override
    public NodeInstanceEntity findByFlowInstanceId(Long aLong) {
        return nodeInstanceEntities.stream()
                .filter(nodeInstanceEntity -> Objects.equals(nodeInstanceEntity.getFlowInstanceId(), aLong))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<NodeInstanceEntity> findByInstanceIdAndInstanceType(Long aLong, FlowNodeType flowNodeType) {
        return nodeInstanceEntities.stream()
                .filter(nodeInstanceEntity -> Objects.equals(nodeInstanceEntity.getInstanceId(), aLong)
                        && Objects.equals(nodeInstanceEntity.getInstanceType(), flowNodeType))
                .collect(Collectors.toList());
    }

    @Override
    public List<NodeInstanceEntity> findByFlowableElementType(FlowableElementType flowableElementType) {
        return nodeInstanceEntities.stream()
                .filter(nodeInstanceEntity -> Objects.equals(nodeInstanceEntity.getFlowableElementType(), flowableElementType))
                .collect(Collectors.toList());
    }

    @Override
    public List<NodeInstanceEntity> findAll(Iterable<Specification<NodeInstanceEntity>> specifications) {
        return nodeInstanceEntities;
    }

    @Override
    public <S extends NodeInstanceEntity> List<S> findAll(Example<S> example) {
        return nodeInstanceEntities.stream()
                .filter(nodeInstanceEntity -> exampleMatches(example, nodeInstanceEntity))
                .map(nodeInstanceEntity -> (S) nodeInstanceEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<NodeInstanceEntity> findAll(Specification<NodeInstanceEntity> specification) {
        return nodeInstanceEntities;
    }

    @Override
    public Page<NodeInstanceEntity> findAll(Pageable pageable, Specification<NodeInstanceEntity> specification) {
        List<NodeInstanceEntity> nodes = nodeInstanceEntities;
        if (specification != null) {
            nodes = new ArrayList<>(nodeInstanceEntities);
            nodes = nodes.stream()
                    .filter(specification.isSatisfiedBy)
                    .collect(Collectors.toList());
        }
        Page<NodeInstanceEntity> page = new PageImpl<>(nodes, pageable, nodes.size());
        return page;
    }

    @Override
    public List<NodeInstanceEntity> findAll(Specification<NodeInstanceEntity> specification1,
            Specification<NodeInstanceEntity>... specifications) {
        List<NodeInstanceEntity> nodes = nodeInstanceEntities;
        if (specification1 != null) {
            nodes = new ArrayList<>(nodeInstanceEntities);
            nodes = nodes.stream()
                    .filter(specification1.isSatisfiedBy)
                    .collect(Collectors.toList());
        }
        if (specifications != null) {
            for (Specification<NodeInstanceEntity> specification : specifications) {
                nodes = nodes.stream()
                        .filter(specification.isSatisfiedBy)
                        .collect(Collectors.toList());
            }
        }
        return nodes;
    }

    @Override
    public <S extends NodeInstanceEntity> List<S> saveAll(Iterable<S> iterable, Callable<Void> callable) throws DataAccessException {
        List<NodeInstanceEntity> nodeInstanceEntitiesToSave = new ArrayList<>();
        iterable.forEach(nodeInstanceEntitiesToSave::add);
        this.nodeInstanceEntities.addAll(nodeInstanceEntitiesToSave);
        callable.call();
        return nodeInstanceEntitiesToSave;
    }

    @Override
    public void flush() {
    }

    @Override
    public void clearStore() {
        nodeInstanceEntities.clear();
    }

    @Override
    public boolean supportsMongoCode() {
        return false;
    }

    @Override
    public <S extends NodeInstanceEntity> S saveAndFlush(S entity) throws DataAccessException {
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntities.add(entity);
        return entity;
    }

    @Override
    public <S extends NodeInstanceEntity> S findOne(Specification<S> specification) {
        return nodeInstanceEntities.stream()
                .filter(specification.isSatisfiedBy)
                .findFirst()
                .map(nodeInstanceEntity -> (S) nodeInstanceEntity)
                .orElse(null);
    }

    @Override
    public int deleteByFlowInstanceId(Long aLong) {
        int deletedEntitiesNum = 0;
        List<NodeInstanceEntity> nodesToDelete = nodeInstanceEntities.stream()
                .filter(nodeInstanceEntity -> Objects.equals(nodeInstanceEntity.getFlowInstanceId(), aLong))
                .collect(Collectors.toList());
        deletedEntitiesNum += nodesToDelete.size();
        nodeInstanceEntities.removeAll(nodesToDelete);
        return deletedEntitiesNum;
    }

    @Override
    public int deleteByInstanceIdAndInstanceType(Long aLong, FlowNodeType flowNodeType) {
        int deletedEntitiesNum = 0;
        List<NodeInstanceEntity> nodesToDelete = nodeInstanceEntities.stream()
                .filter(nodeInstanceEntity -> Objects.equals(nodeInstanceEntity.getInstanceId(), aLong)
                        && Objects.equals(nodeInstanceEntity.getInstanceType(), flowNodeType))
                .collect(Collectors.toList());
        deletedEntitiesNum += nodesToDelete.size();
        nodeInstanceEntities.removeAll(nodesToDelete);
        return deletedEntitiesNum;
    }

    @Override
    public <S extends NodeInstanceEntity> List<S> findAll(Class<S> aClass) {
        return nodeInstanceEntities.stream()
                .map(nodeInstanceEntity -> (S) nodeInstanceEntity)
                .collect(Collectors.toList());
    }

    @Override
    public <S extends NodeInstanceEntity> List<S> findAll(Example<S> example, Sort sort) {
        return nodeInstanceEntities.stream()
                .filter(example::isSatisfiedBy)
                .sorted(sort)
                .map(nodeInstanceEntity -> (S) nodeInstanceEntity)
                .collect(Collectors.toList());
    }

    @Override
    public <S extends NodeInstanceEntity> List<S> findAll(Pageable pageable, Example<S> example) {
        List<NodeInstanceEntity> nodes = nodeInstanceEntities;
        if (example != null) {
            nodes = new ArrayList<>(nodeInstanceEntities);
            nodes = nodes.stream()
                    .filter(example::isSatisfiedBy)
                    .collect(Collectors.toList());
        }
        Page<NodeInstanceEntity> page = new PageImpl<>(nodes, pageable, nodes.size());
        return page.getContent().stream()
                .map(nodeInstanceEntity -> (S) nodeInstanceEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long count(Specification<NodeInstanceEntity> specification) {
        long count = nodeInstanceEntities.size();
        if (specification != null) {
            count = (long) nodeInstanceEntities.stream()
                    .filter(specification.isSatisfiedBy)
                    .count();
        }
        return count;
    }
}