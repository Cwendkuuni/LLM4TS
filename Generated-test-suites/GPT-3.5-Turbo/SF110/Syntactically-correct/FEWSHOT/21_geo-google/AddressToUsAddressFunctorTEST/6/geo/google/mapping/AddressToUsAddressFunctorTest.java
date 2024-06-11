package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        // Create mock dependencies
        GeoAddress addr = mock(GeoAddress.class);
        GeoUsAddress ret = mock(GeoUsAddress.class);
        AddressDetails.Country country = mock(AddressDetails.Country.class);
        AddressDetails.Country.CountryNameCode countryNameCode = mock(AddressDetails.Country.CountryNameCode.class);
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        countryNames.add(countryNameCode);
        AdministrativeArea adminArea = mock(AdministrativeArea.class);
        AdministrativeArea.AdministrativeAreaName adminAreaName = mock(AdministrativeArea.AdministrativeAreaName.class);
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
        adminAreaNames.add(adminAreaName);
        AdministrativeArea.SubAdministrativeArea subAdminArea = mock(AdministrativeArea.SubAdministrativeArea.class);
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = mock(AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName.class);
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAdminAreaNames = new ArrayList<>();
        subAdminAreaNames.add(subAdminAreaName);
        Locality locality = mock(Locality.class);
        Locality.LocalityName localityName = mock(Locality.LocalityName.class);
        List<Locality.LocalityName> localityNames = new ArrayList<>();
        localityNames.add(localityName);
        PostalCode postalCode = mock(PostalCode.class);
        PostalCode.PostalCodeNumber postalCodeNumber = mock(PostalCode.PostalCodeNumber.class);
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        postalCodeNumbers.add(postalCodeNumber);
        Thoroughfare thoroughfare = mock(Thoroughfare.class);
        Thoroughfare.ThoroughfareName thoroughfareName = mock(Thoroughfare.ThoroughfareName.class);
        List<Thoroughfare.ThoroughfareName> thoroughfareNames = new ArrayList<>();
        thoroughfareNames.add(thoroughfareName);
        
        // Set up the mock objects
        when(addr.getAddressDetails()).thenReturn(mock(AddressDetails.class));
        when(addr.getAddressDetails().getCountry()).thenReturn(country);
        when(country.getCountryNameCode()).thenReturn(countryNames);
        when(addr.getAddressDetails().getCountry().getAdministrativeArea()).thenReturn(adminArea);
        when(adminArea.getAdministrativeAreaName()).thenReturn(adminAreaNames);
        when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);
        when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(subAdminAreaNames);
        when(adminArea.getLocality()).thenReturn(locality);
        when(locality.getLocalityName()).thenReturn(localityNames);
        when(locality.getPostalCode()).thenReturn(postalCode);
        when(postalCode.getPostalCodeNumber()).thenReturn(postalCodeNumbers);
        when(locality.getThoroughfare()).thenReturn(thoroughfare);
        when(thoroughfare.getThoroughfareName()).thenReturn(thoroughfareNames);
        
        // Create and execute the functor
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(addr);
        
        // Assert the expected results
        assertEquals(ret, result);
    }
}