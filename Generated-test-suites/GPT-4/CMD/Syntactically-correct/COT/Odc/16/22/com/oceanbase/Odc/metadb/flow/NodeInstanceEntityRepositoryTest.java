package com.oceanbase.odc.metadb.flow;
// Include necessary import statements

public class NodeInstanceEntityRepositoryTest {
  private NodeInstanceEntityRepository repository;

  @Before
  public void setUp() {
    // Initialize repository
    repository = Mockito.mock(NodeInstanceEntityRepository.class);
  }

  @Test
  public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
    Long instanceId = 1L;
    FlowNodeType instanceType = FlowNodeType.START;
    FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
    List<NodeInstanceEntity> list = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    // Assertions or further operations based on list
  }

  @Test
  public void testFindByFlowInstanceId() {
    Long flowInstanceId = 1L;
    List<NodeInstanceEntity> list = repository.findByFlowInstanceId(flowInstanceId);
    // Assertions or further operations based on list
  }

  @Test
  public void testFindByIds() {
    Collection<Long> ids = new ArrayList<>();
    ids.add(1L);
    List<NodeInstanceEntity> list = repository.findByIds(ids);
    // Assertions or further operations based on list
  }

  @Test
  public void testDeleteByFlowInstanceId() {
    Long flowInstanceId = 1L;
    int result = repository.deleteByFlowInstanceId(flowInstanceId);
    // Assertions or further operations based on result
  }

  @Test
  public void testDeleteByInstanceIdAndInstanceType() {
    Long instanceId = 1L;
    FlowNodeType instanceType = FlowNodeType.START;
    int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    // Assertions or further operations based on result
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullValues() {
    repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindByFlowInstanceIdWithNullValue() {
    repository.findByFlowInstanceId(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindByIdsWithNullValue() {
    repository.findByIds(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeleteByFlowInstanceIdWithNullValue() {
    repository.deleteByFlowInstanceId(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeleteByInstanceIdAndInstanceTypeWithNullValues() {
    repository.deleteByInstanceIdAndInstanceType(null, null);
  }
}