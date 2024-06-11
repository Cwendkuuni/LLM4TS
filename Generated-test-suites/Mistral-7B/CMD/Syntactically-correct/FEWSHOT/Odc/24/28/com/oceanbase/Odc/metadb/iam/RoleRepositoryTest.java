package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.SpecificationSupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.jpa.repository.query.ScopedExecutableWrapper;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.query.QueryMethodEvaluationContext;
import org.springframework.data.repository.query.QueryMethodEvaluationContext.QueryMethodTargetType;
import org.springframework.data.util.Pair;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ProjectionFactory factory;

    private RoleRepository testRoleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.testRoleRepository = new JpaRepositoryImplementation<>(RoleEntity.class, Long.class,
                (RoleRepository) this.roleRepository, new SpecificationSupport(), null, null, null,
                new SpelAwareProxyProjectionFactory(), null, null, null, true, null, null);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(Collections.singletonList(id1))).thenReturn(Optional.ofNullable(roleEntities.get(0)));
        when(roleRepository.findByIdIn(Arrays.asList(id1, id2))).thenReturn(Optional.ofNullable(roleEntities));
        List<RoleEntity> result = testRoleRepository.findByIdIn(Arrays.asList(id1, id2));
        verify(roleRepository, times(1)).findByIdIn(Arrays.asList(id1, id2));
        assertEquals(2, result.size());
        assertEquals(roleEntities.get(0), result.get(0));
        assertEquals(roleEntities.get(1), result.get(1));
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByName(name)).thenReturn(Optional.ofNullable(roleEntity));
        RoleEntity result = testRoleRepository.findByName(name);
        verify(roleRepository, times(1)).findByName(name);
        assertEquals(roleEntity, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()), new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 10;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public int getPageCount() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 2;
            }

            @Override
            public long getNumberOfElements() {
                return 2;
            }

            @Override
            public <T> List<T> getContent() {
                return Arrays.asList(new RoleEntity(), new RoleEntity());
            }

            @Override
            public <S> List<S> get(Class<S> clazz) {
                return Arrays.asList(new RoleEntity(), new RoleEntity());
            }
        });
        when(roleRepository.findByOrganizationId(organizationId, any(Pageable.class))).thenReturn(page);
        Page<RoleEntity> result = testRoleRepository.findByOrganizationId(organizationId, new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 10;
            }
        });
        verify(roleRepository, times(1)).findByOrganizationId(organizationId, any(Pageable.class));
        assertEquals(page, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.ofNullable(roleEntity));
        RoleEntity result = testRoleRepository.findByNameAndOrganizationId(name, organizationId);
        verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
        assertEquals(roleEntity, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("name1", "name2");
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);
        List<RoleEntity> result = testRoleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, result.size());
        assertEquals(roleEntities.get(0), result.get(0));
        assertEquals(roleEntities.get(1), result.get(1));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(roleEntities, new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 10;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public int getPageCount() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 2;
            }

            @Override
            public long getNumberOfElements() {
                return 2;
            }

            @Override
            public <T> List<T> getContent() {
                return Arrays.asList(new RoleEntity(), new RoleEntity());
            }

            @Override
            public <S> List<S> get(Class<S> clazz) {
                return Arrays.asList(new RoleEntity(), new RoleEntity());
            }
        });
        ScopedExecutableWrapper<RoleRepository> wrapper = (ScopedExecutableWrapper<RoleRepository>) mock(ScopedExecutableWrapper.class);
        when(wrapper.getTarget()).thenReturn(roleRepository);
        when(wrapper.getQueryMethodEvaluationContext()).thenReturn(new QueryMethodEvaluationContext(wrapper, null, null,
                QueryMethodTargetType.METHOD, null, null, null, null, null, null, null, null, null, null, null, null, null));
        when(wrapper.getNativeQuery()).thenReturn("select r.* from iam_role r where r.organization_id=:organizationId and r.id in (:roleIds)");
        when(wrapper.getCountQuery()).thenReturn("select count(*) from iam_role r where r.organization_id=:organizationId and r.id in (:roleIds)");
        when(wrapper.getParameterValues()).thenReturn(new Object[] { organizationId, roleIds });
        when(roleRepository.findAll(any(Specification.class))).thenCallRealMethod();
        when(roleRepository.getNativeQueryStream(anyString(), any(List.class))).thenCallRealMethod();
        when(roleRepository.getNativeQueryStream(anyString(), anyList())).thenAnswer(invocation -> {
            String query = (String) invocation.getArguments()[0];
            List<Long> params = (List<Long>) invocation.getArguments()[1];
            return testRoleRepository.findAll(scopedExecutable -> {
               assertEquals(wrapper, scopedExecutable.getQueryMethodContext().getScopedExecution());
                assertEquals(RoleRepository.class, scopedExecutable.getTarget().getClass());
                assertEquals(query, scopedExecutable.getNativeQuery());
                assertEquals(params, scopedExecutable.getQueryMethodContext().getParameterValues());
                return Arrays.asList(new RoleEntity(), new RoleEntity()).stream();
            });
        });
        Page<RoleEntity> result = testRoleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 10;
            }
        });
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds, any(Pageable.class));
        assertEquals(page, result);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);
        List<RoleEntity> result = testRoleRepository.findByUserId(userId);
        verify(roleRepository, times(1)).findByUserId(userId);
        assertEquals(2, result.size());
        assertEquals(roleEntities.get(0), result.get(0));
        assertEquals(roleEntities.get(1), result.get(1));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);
        List<RoleEntity> result = testRoleRepository.findByUserIdAndEnabled(userId, enabled);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(userId, enabled);
        assertEquals(2, result.size());
        assertEquals(roleEntities.get(0), result.get(0));
        assertEquals(roleEntities.get(1), result.get(1));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);
        List<RoleEntity> result = testRoleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(2, result.size());
        assertEquals(roleEntities.get(0), result.get(0));
        assertEquals(roleEntities.get(1), result.get(1));
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        testRoleRepository.update(roleEntity);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}