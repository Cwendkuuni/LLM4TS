package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;

public class ListPopulationTest {

    @Test
    public void testNewPopulationWithPositiveSizeLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertNull(population.getChromosomes().isEmpty());
    }

    @Test
    public void testNewPopulationWithEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(5, population.getPopulationLimit());
        assertFalse(population.getChromosomes().isEmpty());
    }

    @Test
    public void testSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(5);
        assertThrows(NullArgumentException.class, () -> population.setChromosomes(null));
    }

    @Test
    public void testSetChromosomesWithListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> bigList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            bigList.add(new Chromosome() {
                @Override
                public int getFitness() {
                    return i;
                }

                @Override
                public String toString() {
                    return "Chromosome " + i;
                }
            });
        }
        assertThrows(NumberIsTooLargeException.class, () -> population.setChromosomes(bigList));
    }

    @Test
    public void testSetChromosomesWithListSmallerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> smallList = new ArrayList<>();
        smallList.add(new Chromosome() {
            @Override
            public int getFitness() {
                return 1;
            }

            @Override
            public String toString() {
                return "Chromosome 0";
            }
        });
        population.setChromosomes(smallList);
        assertEquals(1, population.getPopulationSize());
        assertEquals(smallList, population.getChromosomes());
    }

    @Test
    public void testAddChromosomeWithNullChromosome() {
        ListPopulation population = new ListPopulation(5);
        assertThrows(NullArgumentException.class, () -> population.addChromosome(null));
    }

    @Test
    public void testAddChromosomeWithPopulationLimitReached() {
        ListPopulation population = new ListPopulation(5);
        for (int i = 0; i < 5; i++) {
            population.addChromosome(new Chromosome() {
                @Override
                public int getFitness() {
                    return i;
                }

                @Override
                public String toString() {
                    return "Chromosome " + i;
                }
            });
        }
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosome(new Chromosome() {
            @Override
            public int getFitness() {
                return 5;
            }

            @Override
            public String toString() {
                return "Chromosome 5";
            }
        }));
    }

    @Test
    public void testAddChromosomesWithNullCollection() {
        ListPopulation population = new ListPopulation(5);
        assertThrows(NullArgumentException.class, () -> population.addChromosomes(null));
    }

    @Test
    public void testAddChromosomesWithCollectionBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> bigList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            bigList.add(new Chromosome() {
                @Override
                public int getFitness() {
                    return i;
                }

                @Override
                public String toString() {
                    return "Chromosome " + i;
                }
            });
        }
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosomes(bigList));
    }

    @Test
    public void testAddChromosomesWithCollectionSmallerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> smallList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            smallList.add(new Chromosome() {
                @Override
                public int getFitness() {
                    return i;
                }

                @Override
                public String toString() {
                    return "Chromosome " + i;
                }
            });
        }
        population.addChromosomes(smallList);
        assertEquals(3, population.getPopulationSize());
        assertSame(smallList, population.getChromosomes());
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> expectedList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new Chromosome() {
                @Override
                public int getFitness() {
                    return i;
                }

                @Override
                public String toString() {
                    return "Chromosome " + i;
                }
            };
            population.addChromosome(chromosome);
            expectedList.add(chromosome);
        }
        List<Chromosome> actualList = population.getChromosomes();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {
            @Override
            public int getFitness() {
                return 0;
            }

            @Override
            public String toString() {
                return "Chromosome 0";
            }
        });
        List<Chromosome> actualList = population.getChromosomeList();
        List<Chromosome> expectedList = new ArrayList<>();
        expectedList.add(new Chromosome() {
            @Override
            public int getFitness() {
                return 0;
            }

            @Override
            public String toString() {
                return "Chromosome 0";
            }
        });
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome fittestChromosome = new Chromosome() {
            @Override
            public int getFitness() {
                return 100;
            }

            @Override
            public String toString() {
                return "Fittest Chromosome";
            }
        };
        Chromosome secondFittestChromosome = new Chromosome() {
            @Override
            public int getFitness() {
                return 99;
            }

            @Override
            public String toString() {
                return "Second Fittest Chromosome";
            }
        };
        Chromosome thirdFittestChromosome = new Chromosome() {
            @Override
            public int getFitness() {
                return 98;
            }

            @Override
            public String toString() {
                return "Third Fittest Chromosome";
            }
        };
        population.addChromosome(fittestChromosome);
        population.addChromosome(secondFittestChromosome);
        population.addChromosome(thirdFittestChromosome);
        Chromosome actualFittestChromosome = population.getFittestChromosome();
        assertTrue(actualFittestChromosome.compareTo(fittestChromosome) == 0);
    }

    @Test
    public void testSetPopulationLimitWithNegativeNumber() {
        ListPopulation population = new ListPopulation(5);
        assertThrows(NotPositiveException.class, () -> population.setPopulationLimit(-1));
    }

    @Test
    public void testSetPopulationLimitWithSameSize() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitWithBiggerNumber() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitWithSmallerNumber() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
        assertThrows(NumberIsTooSmallException.class, () -> population.setPopulationLimit(3));
    }
}