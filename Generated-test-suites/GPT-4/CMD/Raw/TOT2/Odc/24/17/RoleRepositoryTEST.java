import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
public class RoleRepositoryTest {

  @InjectMocks
  private RoleRepository roleRepository;

  @Mock
  private RoleEntity mockRoleEntity;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void findByIdInTest() {
    List<Long> ids = Arrays.asList(1L, 2L, 3L);
    when(roleRepository.findByIdIn(ids)).thenReturn(null);
    verify(roleRepository).findByIdIn(ids);
  }

  @Test
  public void findByNameTest() {
    String name = "test";
    when(roleRepository.findByName(name)).thenReturn(null);
    verify(roleRepository).findByName(name);
  }
  
  //TODO Add other methods similarly

  @Test
  public void updateRoleEntityTest() {
    RoleEntity roleEntity = new RoleEntity();
    roleEntity.setId(1L);
    roleEntity.setName("Manager");
    roleEntity.setEnabled(true);
    roleEntity.setDescription("Description");

    when(roleRepository.update(roleEntity)).thenReturn(1);
    verify(roleRepository).update(roleEntity);
  }
}