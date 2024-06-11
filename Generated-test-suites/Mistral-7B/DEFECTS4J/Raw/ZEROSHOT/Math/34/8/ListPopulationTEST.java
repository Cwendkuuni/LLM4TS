import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        new ListPopulation(-5);
    }

    @Test
    public void testListPopulationWithEmptyList() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testListPopulationWithInitialList() {
        List<Chromosome> initialList = new ArrayList<>();
        initialList.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation population = new ListPopulation(initialList, 10);
        assertEquals(initialList.size(), population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullInitialList() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithListBiggerThanPopulationLimit() {
        List<Chromosome> initialList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            initialList.add(new Chromosome());
        }
        new ListPopulation(initialList, 10);
    }

    @Test
    public void testListPopulationWithAddChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(new ArrayList<>());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
        population.addChromosomes(new ArrayList<>());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
        List<Chromosome> list = new ArrayList<>();
        list.add(new Chromosome());
        population.addChromosomes(list);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
        population.addChromosomes(new ArrayList<>());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithListBiggerThanPopulationLimitWhenAddingChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(new Chromosome());
        }
        population.addChromosomes(list);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        List<Chromosome> initialList = new ArrayList<>();
        initialList.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation population = new ListPopulation(initialList, 10);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(initialList, chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        List<Chromosome> initialList = new ArrayList<>();
        initialList.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation population = new ListPopulation(initialList, 10);
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals(initialList, chromosomes);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> initialList = new ArrayList<>();
        initialList.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(initialList);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(initialList, chromosomes);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithListBiggerThanPopulationLimit() {
        List<Chromosome> initialList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            initialList.add(new Chromosome());
        }
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(initialList);
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        assertEquals(11, population.getPopulationLimit());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWhenPopulationLimitReached() {
        ListPopulation population = new ListPopulation(10);
        for (int i = 0; i < 10; i++) {
            population.addChromosome(new Chromosome());
        }
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        List<Chromosome> initialList = new ArrayList<>();
        initialList.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        initialList.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        ListPopulation population = new ListPopulation(initialList, 10);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(initialList.get(1), fittestChromosome);
    }

    @Test
    public void testListPopulationToString() {
        List<Chromosome> initialList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            initialList.add(new Chromosome() {
                @Override
                public double getFitness() {
                    return i;
                }
            });
        }
        ListPopulation population = new ListPopulation(initialList, 10);
        assertEquals("[Chromosome: fitness=0.0, Chromosome: fitness=1.0, Chromosome: fitness=2.0, Chromosome: fitness=3.0, Chromosome: fitness=4.0, Chromosome: fitness=5.0, Chromosome: fitness=null, Chromosome: fitness=null, Chromosome: fitness=null, Chromosome: fitness=null]", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        List<Chromosome> initialList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            initialList.add(new Chromosome() {
                @Override
                public double getFitness() {
                    return i;
                }
            });
        }
        ListPopulation population = new ListPopulation(initialList, 10);
        Iterator<Chromosome> iterator = population.iterator();
        List<Chromosome> chromosomes = new ArrayList<>();
        while (iterator.hasNext()) {
            chromosomes.add(iterator.next());
        }
        assertEquals(initialList, chromosomes);
    }

}