package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithEmptyListAndLimit() {
        ListPopulation listPopulation = new ListPopulation(5);
        assertEquals(5, listPopulation.getPopulationLimit());
        assertTrue(listPopulation.getChromosomes().isEmpty());
    }

    @Test
    public void testListPopulationWithListAndLimit() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });

        ListPopulation listPopulation = new ListPopulation(initialChromosomes, 5);

        assertEquals(5, listPopulation.getPopulationLimit());
        assertEquals(initialChromosomes.size(), listPopulation.getPopulationSize());
        assertEquals(initialChromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullList() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        new ListPopulation(Collections.emptyList(), -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithListBiggerThanLimit() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 2;
            }
        });

        new ListPopulation(initialChromosomes, 2);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });

        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 5);
        listPopulation.setChromosomes(initialChromosomes);

        assertEquals(initialChromosomes.size(), listPopulation.getPopulationSize());
        assertEquals(initialChromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullList() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 5);
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithListBiggerThanLimit() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 2;
            }
        });

        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 2);
        listPopulation.setChromosomes(initialChromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 5);
        listPopulation.addChromosomes(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }));

        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }), listPopulation.getChromosomes());
    }

    @Test
    public void testAddChromosomesWithNullCollection() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 5);
        listPopulation.addChromosomes(null);
        assertTrue(listPopulation.getChromosomes().isEmpty());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithListBiggerThanLimit() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 2);
        listPopulation.addChromosomes(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }));
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        });

        ListPopulation listPopulation = new ListPopulation(initialChromosomes, 5);

        List<Chromosome> result = listPopulation.getChromosomes();
        assertEquals(initialChromosomes, result);
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation listPopulation = new ListPopulation(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 2;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 3;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 4;
            }
        }), 5);

        List<Chromosome> result = listPopulation.getChromosomeList();
        assertEquals(5, result.size());
        assertEquals(listPopulation.getChromosomes(), result);
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 5);
        listPopulation.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        });

        assertEquals(1, listPopulation.getPopulationSize());
        assertEquals(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }), listPopulation.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithPopulationLimitReached() {
        ListPopulation listPopulation = new ListPopulation(Collections.emptyList(), 1);
        listPopulation.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        });
        listPopulation.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        });
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        }), 5);

        assertEquals(listPopulation.getChromosomes().get(1), listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        }), 5);

        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitWithSameSize() {
        ListPopulation listPopulation = new ListPopulation(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        }), 5);

        int populationLimit = 5;
        listPopulation.setPopulationLimit(populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeNumber() {
        ListPopulation listPopulation = new ListPopulation(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        }), 5);

        listPopulation.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithPopulationBiggerThanLimit() {
        ListPopulation listPopulation = new ListPopulation(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        }), 5);

        listPopulation.setPopulationLimit(3);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return -1;
            }
        }), 5);

        assertEquals(3, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }

            @Override
            public String toString() {
                return "Chromosome [id=" + hashCode() + "]";
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }

            @Override
            public String toString() {
                return "Chromosome [id=" + hashCode() + "]";
            }
        });

        ListPopulation listPopulation = new ListPopulation(initialChromosomes, 5);

        assertEquals("[Chromosome [id=" + initialChromosomes.get(0).hashCode() + "], Chromosome [id=" + initialChromosomes.get(1).hashCode() + "]]", listPopulation.toString());
    }

    @Test
    public void testIterator() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 0;
            }

            @Override
            public String toString() {
                return "Chromosome [id=" + hashCode() + "]";
            }
        }, new Chromosome() {
            @Override
            public int compareTo(Chromosome o) {
                return 1;
            }

            @Override
            public String toString() {
                return "Chromosome [id=" + hashCode() + "]";
            }
        });

        ListPopulation listPopulation = new ListPopulation(initialChromosomes, 5);
        Iterator<Chromosome> iterator = listPopulation.iterator();

        assertEquals(initialChromosomes.get(0), iterator.next());
        assertEquals(initialChromosomes.get(1), iterator.next());
        assertFalse(iterator.hasNext());
    }
}