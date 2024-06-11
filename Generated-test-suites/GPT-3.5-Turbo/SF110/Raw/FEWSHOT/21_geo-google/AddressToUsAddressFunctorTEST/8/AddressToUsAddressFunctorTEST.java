package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import geo.google.GeoException;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.Locality;
import geo.google.datamodel.PostalCode;
import geo.google.datamodel.ThoroughfareNameType;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails.Country.CountryNameCode;


public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() throws GeoException {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = mock(GeoAddress.class);
        GeoUsAddress ret = new GeoUsAddress();

        // Mock AddressDetails
        AddressDetails addressDetails = mock(AddressDetails.class);
        when(addr.getAddressDetails()).thenReturn(addressDetails);

        // Mock Country
        Country country = mock(Country.class);
        when(addressDetails.getCountry()).thenReturn(country);

        // Mock CountryNameCode list
        CountryNameCode countryNameCode = mock(CountryNameCode.class);
        when(country.getCountryNameCode()).thenReturn(CollectionUtils.asList(countryNameCode));

        // Mock AdministrativeArea
        AdministrativeArea adminArea = mock(AdministrativeArea.class);
        when(country.getAdministrativeArea()).thenReturn(adminArea);

        // Mock AdministrativeAreaName list
        AdministrativeArea.AdministrativeAreaName adminAreaName = mock(AdministrativeArea.AdministrativeAreaName.class);
        when(adminArea.getAdministrativeAreaName()).thenReturn(CollectionUtils.asList(adminAreaName));

        // Mock SubAdministrativeArea
        AdministrativeArea.SubAdministrativeArea subAdminArea = mock(AdministrativeArea.SubAdministrativeArea.class);
        when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);

        // Mock SubAdministrativeAreaName list
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = mock(AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName.class);
        when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(CollectionUtils.asList(subAdminAreaName));

        // Mock Locality
        Locality locality = mock(Locality.class);
        when(subAdminArea.getLocality()).thenReturn(locality);

        // Mock LocalityName list
        Locality.LocalityName localityName = mock(Locality.LocalityName.class);
        when(locality.getLocalityName()).thenReturn(CollectionUtils.asList(localityName));

        // Mock PostalCode
        PostalCode postalCode = mock(PostalCode.class);
        when(locality.getPostalCode()).thenReturn(postalCode);

        // Mock PostalCodeNumber list
        PostalCode.PostalCodeNumber postalCodeNumber = mock(PostalCode.PostalCodeNumber.class);
        when(postalCode.getPostalCodeNumber()).thenReturn(CollectionUtils.asList(postalCodeNumber));
      
        // Mock Thoroughfare
        ThoroughfareNameType thoroughfare = mock(ThoroughfareNameType.class);
        when(locality.getThoroughfare()).thenReturn(thoroughfare);

        // Mock ThoroughfareNameType list
        ThoroughfareNameType thoroughfareNameType = mock(ThoroughfareNameType.class);
        when(thoroughfare.getThoroughfareName()).thenReturn(CollectionUtils.asList(thoroughfareNameType));

        // Test method
        assertEquals(ret, functor.execute(addr));
    }
}